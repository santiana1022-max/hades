package fun.hades.security.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.entity.SysUser;
import fun.hades.security.LoginUser;
import fun.hades.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security 用户查询实现（企业级：仅负责认证时的用户查询）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService sysUserService;
    private final SysUserRoleService sysUserRoleService;
    private final SysRoleMenuService sysRoleMenuService;
    private final SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询用户（复用你现有方法）
        SysUser user = sysUserService.getUserByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(StatusCodeEnum.USER_NOT_FOUND.getMsg());
        }

        // 2. 核心：查询用户的权限标识（用户→角色→菜单→perms）
        // 2.1 用户→角色ID
        List<Long> roleIds = sysUserRoleService.listRoleIdsByUserId(user.getId());
        if (CollectionUtils.isEmpty(roleIds)) {
            return new LoginUser(user.getId(), user.getUsername(), user.getPassword(), Arrays.asList());
        }
        log.info("roleIds ===================> {}", roleIds);
        // 2.2 角色→菜单ID
        List<Long> menuIds = sysRoleMenuService.listMenuIdsByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return new LoginUser(user.getId(), user.getUsername(), user.getPassword(),Arrays.asList());
        }
        log.info("menuIds ===================> {}", menuIds);
        // 2.3 菜单→权限标识（perms）
        List<String> perms = sysMenuService.listPermsByMenuIds(menuIds);


        log.info("perms ===================> {}", perms);
        // 3. 封装为Spring Security权限对象
        List<SimpleGrantedAuthority> authorities = perms.stream()
                .map(SimpleGrantedAuthority::new) // 权限标识→框架识别的权限对象
                .collect(Collectors.toList());


        log.info("authorities ===================> {}", authorities);

        // 3. 返回自定义认证主体（仅传必需字段）
        return new LoginUser(user.getId(), user.getUsername(), user.getPassword(), authorities);
    }
}