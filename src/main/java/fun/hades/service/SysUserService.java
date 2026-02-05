package fun.hades.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.hades.dto.SysUserQueryDTO;
import fun.hades.dto.response.SysUserDTO;
import fun.hades.entity.SysUser;

public interface SysUserService {

    /*
    获取用户信息
     */
    SysUserDTO getUserInfo();

    /**
     * 分页查询用户列表
     */
    Page<SysUserDTO> getUserPage(SysUserQueryDTO sysUserQueryDTO);






    /**
     * 通用方法：根据账号（用户名/手机号/邮箱）查询用户
     */
    SysUser getUserByAccount(String account);

}
