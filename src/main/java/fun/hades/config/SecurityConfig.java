package fun.hades.config;

import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.common.filter.JwtAuthenticationFilter;
import fun.hades.entity.SysUser;
import fun.hades.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 密码加密配置（仅用于密码验证，无安全拦截）
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 注入BCrypt密码加密器（Spring官方推荐）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. 认证管理器（登录时验证用户名密码）
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 3. 用户详情服务（从数据库查询用户，适配你的SysUserService）
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // 调用你已有的getUserByAccount方法查询用户
            SysUser user = sysUserService.getUserByAccount(username);
            if (user == null) {
                throw new RuntimeException(StatusCodeEnum.USER_NOT_FOUND.getMsg());
            }
            // 封装为Spring Security的UserDetails（密码用加密后的）
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword()) // 数据库中必须是BCrypt加密密码
                    .authorities("ADMIN") // 角色（可扩展）
                    .build();
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭CSRF防护
                .csrf(csrf -> csrf.disable())
                // 配置请求权限
                .authorizeHttpRequests(auth -> auth
                        // 【核心】放行登录接口
                        .antMatchers("/api/sys/login").permitAll()
                        // 其余接口暂时放行
                        .anyRequest().authenticated()
                )
                // 无状态会话
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 添加JWT过滤器，在用户名密码认证过滤器之前执行
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
