package fun.hades.common.filter;

import fun.hades.common.util.JwtUtil;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fun.hades.common.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

/**
 * JWT认证过滤器（拦截请求，验证令牌）
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 打印接收到的完整Token + 服务器当前时间
        String authHeader = request.getHeader(jwtUtil.getHeader());
        System.out.println("【1. 接收到的请求头】Authorization: " + authHeader);
        Date nowCst = new Date();
        long nowUtc = nowCst.getTime() - 8 * 3600 * 1000;
        System.out.println("【1. 服务器当前时间】CST: " + nowCst + ", UTC: " + new Date(nowUtc));
        System.out.println("【1. 新Token有效期】CST: 2026-02-05 01:08 ~ 03:08, UTC: 2026-02-04 17:08 ~ 19:08");


        // 1. 从请求头获取令牌
//        String authHeader = request.getHeader(jwtUtil.getHeader());
        String token = null;
        String username = null;
        String pureToken = null;

        // 2. 解析令牌（判断是否以指定前缀开头）
        if (authHeader != null && authHeader.startsWith(jwtUtil.getPrefix())) {
//            token = authHeader.substring(jwtUtil.getPrefix().length()); // 去掉前缀
            token = authHeader;
            pureToken =token.substring(jwtUtil.getPrefix().length());
            try {
                System.out.println("【3. 准备解析】纯Token: " + pureToken + ", 服务器当前CST时间: " + nowCst);
//                username = redisUtil.getLogintoken(token);
                username = jwtUtil.extractUsername(pureToken);
            } catch (ExpiredJwtException e) {
                // 打印过期异常详情
                System.err.println("【解析失败-TOKEN过期】新Token本应有效期：2026-02-05 01:08~03:08 CST");
                System.err.println("【过期异常详情】Token实际exp时间: " + e.getClaims().getExpiration()
                        + ", 服务器当前时间: " + new Date()
                        + ", 时间差: " + (new Date().getTime() - e.getClaims().getExpiration().getTime()) + "ms");
                e.printStackTrace(); // 打印异常堆栈
            }catch (Exception e) {
                // 令牌解析失败（过期、签名错误等），直接放行，后续Security会返回401
                System.err.println("【解析失败-其他异常】pureToken: " + pureToken);
                logger.error("token解析失败：" + e.getMessage());
            }
        }

        // 3. 验证令牌并设置认证信息（仅当用户未认证时处理）
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 判断JWT 签名/过期时间有效 & Redis 中存在该 Token
            if (jwtUtil.validateToken(pureToken, userDetails) && redisUtil.hasLoginToken(token)) {
                //token有效，将用户信息放入Security上下文
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        // 4. 放行后续过滤器
        filterChain.doFilter(request, response);
    }
}
