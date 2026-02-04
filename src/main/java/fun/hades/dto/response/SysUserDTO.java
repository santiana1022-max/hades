package fun.hades.dto.response;

import fun.hades.entity.SysUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * 用户信息DTO（脱敏后，返回给前端）
 */
@Data
public class SysUserDTO {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 微信openid
     */
    private String wxOpenid;
    /**
     * 头像URL
     */
    private String avatar;
    /**
     * 账号状态：0-禁用，1-正常
     */
    private Integer status;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 角色列表（多对多关联）
     */
//    private List<SysRole> roleList;

    /**
     * 通用方法：将SysUser转换为SysUserDTO，并查询角色列表
     * @param user 原始用户实体
     * @return 脱敏后的用户DTO
     */
    public static SysUserDTO convertToDTO(SysUser user) {
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
}
