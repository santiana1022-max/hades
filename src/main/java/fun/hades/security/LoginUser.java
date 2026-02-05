package fun.hades.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 自定义登录用户信息（企业级精简版：仅认证必需字段）
 */
@Data
@AllArgsConstructor
public class LoginUser implements UserDetails {

    /**
     * 核心：用户ID（从上下文提取的关键数据）
     */
    private final Long userId;

    /**
     * 用户名
     */
    private final String username;

    /**
     * 密码（Spring Security 校验必需，不返回前端）
     */
    private final String password;

    /**
     * 权限集合（Spring Security 权限控制必需）
     */
    private final Collection<? extends GrantedAuthority> authorities;

    // ==================== 固定实现（无需修改，企业级标准写法） ====================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}