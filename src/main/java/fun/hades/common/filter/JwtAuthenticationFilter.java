package fun.hades.common.filter;

import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.common.util.JSONUtil;
import fun.hades.common.util.JwtUtil;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fun.hades.common.util.RedisUtil;
import fun.hades.config.IgnoreUrlsConfig;
import fun.hades.security.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器（拦截请求，验证令牌）
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig; // 注入白名单配置类

    /**
     * 【核心自定义方法】统一返回错误响应：设置 401 状态码 + JSON 格式提示
     * @param response 响应对象（用于写入错误信息）
     * @param statusCodeEnum 错误枚举（包含错误码、提示信息）
     */
    private void responseError(HttpServletResponse response, StatusCodeEnum statusCodeEnum) throws IOException {
        // 1. 设置响应头：告诉前端返回的是 JSON 格式，编码为 UTF-8（避免乱码）
        response.setContentType("application/json;charset=UTF-8");
        // 2. 核心：设置 HTTP 状态码为 401（认证失败，对应 token 异常场景）
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 3. 将响应体转为 JSON 字符串，写入响应流（前端就能收到错误提示）
        response.getWriter().write(JSONUtil.toJsonStr(Result.fail(statusCodeEnum)));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 判断API在白名单，不验证token直接放行
        if (ignoreUrlsConfig.isIgnoreUrl( request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. 从请求头获取令牌
        String authHeader = request.getHeader(jwtUtil.getHeader());
        String token = null;
        String username = null;
        String pureToken = null;

        if (authHeader == null || !StringUtils.hasText(authHeader)){
            responseError(response,StatusCodeEnum.TOKEN_IS_EMPTY);
            return;
        }
        if (!authHeader.startsWith(jwtUtil.getPrefix())){
            responseError(response,StatusCodeEnum.TOKEN_INVALID);
            return;
        }


        // token 带前缀
        token = authHeader;
        // token 去掉前缀
        pureToken = token.substring(jwtUtil.getPrefix().length());
        try {
            // 从token中获取用户名
            username = jwtUtil.extractUsername(pureToken);
        } catch (Exception e) {
            // 令牌解析失败（过期、签名错误等），直接放行，后续Security会返回401
            log.error("token解析失败：" + e.getMessage());
            responseError(response,StatusCodeEnum.TOKEN_INVALID);
            return;
        }

        // 3. 验证令牌并设置认证信息（仅当用户未认证时处理）
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.info("userDetails ================{}", userDetails);


            log.info("jwtUtil.validateToken(pureToken, userDetails) === {}",jwtUtil.validateToken(pureToken, userDetails));
            log.info(" redisUtil.hasLoginToken(token) === {}", redisUtil.hasLoginToken(token));

            // 判断JWT 签名/过期时间有效 & Redis 中存在该 Token
            if (jwtUtil.validateToken(pureToken, userDetails) && redisUtil.hasLoginToken(token)) {

                if (jwtUtil.isNeedRenew(pureToken)) {
                    log.info("jwtUtil.isNeedRenew(pureToken) = {}",jwtUtil.isNeedRenew(pureToken));
                    String newToken = jwtUtil.getPrefix()+ jwtUtil.generateToken(userDetails);
                    redisUtil.deleteLoginToken(token);
                    redisUtil.setLoginToken(newToken,username,jwtUtil.getExpire());
                    //  将新token放入响应头（TODO:前端需监听该header，替换本地token）
                    response.setHeader("New-Authorization", newToken);
                    log.info("续签成功，新token已返回，用户名：{}", username);
                }

                //token有效，将用户信息放入Security上下文
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);


                log.info("JWT过滤器：用户 {} 权限已存入上下文，权限列表：{}",
                        userDetails.getUsername(), userDetails.getAuthorities());

            }else {
                responseError(response,StatusCodeEnum.TOKEN_EXPIRED);
                return;
            }
        }else {
            responseError(response,StatusCodeEnum.TOKEN_NOT_EXIST);
            return;
        }


        // 4. 放行后续过滤器
        filterChain.doFilter(request, response);
    }
}
