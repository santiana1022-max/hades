package fun.hades.service.impl;

import fun.hades.dto.response.SysUserDTO;
//import fun.hades.entity.SysRole;
import fun.hades.entity.SysUser;
import fun.hades.mapper.SysUserMapper;
import fun.hades.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户Service实现类
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; // 注入BCrypt加密器

    /**
     * 【核心】三合一登录实现
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务保证登录信息更新原子性
    public SysUserDTO login(String account, String password, String loginIp) {
        // 1. 根据账号查询用户
        SysUser user = getUserByAccount(account);
        if (user == null) {
            return null; // 用户不存在
        }


        // 2. 验证密码（BCrypt手动匹配盐值，处理salt）
        String rawPasswordWithSalt = password.trim() + user.getSalt().trim();
        boolean isPasswordMatch = passwordEncoder.matches(rawPasswordWithSalt, user.getPassword());
        if (!isPasswordMatch) {
            return null; // 密码错误
        }

        // 3. 验证账号状态（0-禁用，1-正常）
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }

        // 4. 更新登录信息（登录IP、登录时间、登录次数）
        user.setLoginIp(loginIp);
        user.setLoginTime(LocalDateTime.now());
        user.setLoginCount(user.getLoginCount() + 1);
        sysUserMapper.updateById(user); // 执行更新

        // 5. 转换为DTO并返回
        return convertToDTO(user);
    }

    /**
     * 通用方法：根据账号（用户名/手机号/邮箱）查询用户
     */
    private SysUser getUserByAccount(String account) {
        // 按优先级查询：用户名 > 手机号 > 邮箱
        SysUser user = sysUserMapper.selectByUsername(account);
        if (user == null) {
            user = sysUserMapper.selectByPhone(account);
        }
        if (user == null) {
            user = sysUserMapper.selectByEmail(account);
        }
        return user;
    }

    /**
     * 通用方法：将SysUser转换为SysUserDTO，并查询角色列表
     * @param user 原始用户实体
     * @return 脱敏后的用户DTO
     */
    private SysUserDTO convertToDTO(SysUser user) {
        if (user == null) {
            return null;
        }
        SysUserDTO userDTO = new SysUserDTO();
        // 复制非敏感字段（BeanUtils自动匹配属性名）
        BeanUtils.copyProperties(user, userDTO);
        // 查询用户角色列表
//        List<SysRole> roleList = sysUserMapper.selectRolesByUserId(user.getId());
//        userDTO.setRoleList(roleList);
        return userDTO;
    }

    @Override
    public SysUserDTO getByUsername(String username) {
        SysUser user = sysUserMapper.selectByUsername(username);
        return convertToDTO(user);
    }

    @Override
    public SysUserDTO getByPhone(String phone) {
        SysUser user = sysUserMapper.selectByPhone(phone);
        return convertToDTO(user);
    }

    @Override
    public SysUserDTO getByEmail(String email) {
        SysUser user = sysUserMapper.selectByEmail(email);
        return convertToDTO(user);
    }
}
