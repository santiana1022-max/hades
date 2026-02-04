package fun.hades.service.impl;

import fun.hades.dto.response.SysUserDTO;
//import fun.hades.entity.SysRole;
import fun.hades.entity.SysUser;
import fun.hades.mapper.SysUserMapper;
import fun.hades.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 通用方法：根据账号（用户名/手机号/邮箱）查询用户
     */
    public SysUser getUserByAccount(String account) {
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

    @Override
    public SysUserDTO getByUsername(String username) {
        SysUser user = sysUserMapper.selectByUsername(username);
        return SysUserDTO.convertToDTO(user);
    }

    @Override
    public SysUserDTO getByPhone(String phone) {
        SysUser user = sysUserMapper.selectByPhone(phone);
        return SysUserDTO.convertToDTO(user);
    }

    @Override
    public SysUserDTO getByEmail(String email) {
        SysUser user = sysUserMapper.selectByEmail(email);
        return SysUserDTO.convertToDTO(user);
    }
}
