package fun.hades.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<SysRoleDTO> roleList;
}
