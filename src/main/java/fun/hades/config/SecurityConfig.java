package fun.hades.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 密码加密配置（仅用于密码验证，无安全拦截）
 */
@Configuration
public class SecurityConfig {

    /**
     * 注入BCrypt密码加密器（Spring官方推荐）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 配置请求权限
                .authorizeHttpRequests(auth -> auth
                        // 【核心】放行登录接口
                        .antMatchers("/api/user/login").permitAll()
                        // 其余接口暂时放行
                        .anyRequest().permitAll()
                )
                // 关闭CSRF防护
                .csrf(csrf -> csrf.disable())
                // 无状态会话
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
