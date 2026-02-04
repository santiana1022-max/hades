package fun.hades.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.hades.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Mapper接口
 * 继承BaseMapper，自动拥有基础CRUD方法
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    // 可在此处添加自定义SQL方法（如根据用户名查询用户、根据微信openid查询用户等）
    /**
     * 根据用户名查询用户（Spring Security认证专用）
     * @param username 用户名
     * @return 用户信息
     */
    SysUser selectByUsername(String username);

    /**
     * 根据微信openid查询用户（微信登录专用）
     * @param wxOpenid 微信openid
     * @return 用户信息
     */
    SysUser selectByWxOpenid(String wxOpenid);

    // 根据手机号查询用户（新增）
    SysUser selectByPhone(String phone);

    // 根据邮箱查询用户（新增）
    SysUser selectByEmail(String email);

    // 根据用户ID查询角色列表
//    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
}
