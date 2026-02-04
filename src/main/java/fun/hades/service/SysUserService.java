package fun.hades.service;

import fun.hades.dto.response.SysUserDTO;
import fun.hades.entity.SysUser;

public interface SysUserService {

    /**
     * 三合一登录逻辑
     * @param account  账号（用户名/手机号/邮箱）
     * @param password 明文密码
     * @param loginIp  登录IP
     * @return 脱敏后的用户DTO（登录成功），null（登录失败）
     */
//    SysUserDTO login(String account, String password, String loginIp);

    /**
     * 通用方法：根据账号（用户名/手机号/邮箱）查询用户
     */
    SysUser getUserByAccount(String account);

    /**
     * 根据用户名查询用户信息（含角色）
     * @param username 用户名
     * @return 脱敏后的用户DTO
     */
    SysUserDTO getByUsername(String username);

    /**
     * 根据手机号查询用户信息（含角色）
     * @param phone 手机号
     * @return 脱敏后的用户DTO
     */
    SysUserDTO getByPhone(String phone);

    /**
     * 根据邮箱查询用户信息（含角色）
     * @param email 邮箱
     * @return 脱敏后的用户DTO
     */
    SysUserDTO getByEmail(String email);
}
