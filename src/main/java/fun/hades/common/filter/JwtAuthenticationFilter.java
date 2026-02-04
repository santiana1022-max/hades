package fun.hades.common.filter;

import fun.hades.common.util.JwtUtil;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

/**
 * JWT认证过滤器（拦截请求，验证令牌）
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 从请求头获取令牌
        String authHeader = request.getHeader(jwtUtil.getHeader());
        String token = null;
        String username = null;

        // 2. 解析令牌（判断是否以指定前缀开头）
        if (authHeader != null && authHeader.startsWith(jwtUtil.getPrefix())) {
            token = authHeader.substring(jwtUtil.getPrefix().length()); // 去掉前缀
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // 令牌解析失败（过期、签名错误等），直接放行，后续Security会返回401
                logger.error("JWT令牌解析失败：" + e.getMessage());
            }
        }

        // 3. 验证令牌并设置认证信息（仅当用户未认证时处理）
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 令牌有效，将用户信息放入Security上下文
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 4. 放行后续过滤器
        filterChain.doFilter(request, response);
    }
}
