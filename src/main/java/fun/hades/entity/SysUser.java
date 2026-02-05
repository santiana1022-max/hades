package fun.hades.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户实体类（测试平台）
 * 对应数据库表：sys_user
 */
@Data
@TableName("sys_user")
public class SysUser {

    /**
     * 主键ID（
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（唯一，登录账号）
     */
    @TableField("username")
    private String username;

    /**
     * 加密密码（BCrypt加密）
     */
    @TableField("password")
    private String password;

    /**
     * 手机号（唯一）
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱（唯一）
     */
    @TableField("email")
    private String email;

    /**
     * 微信openid（唯一）
     */
    @TableField("wx_openid")
    private String wxOpenid;

    /**
     * 微信unionid（多应用统一标识）
     */
    @TableField("wx_unionid")
    private String wxUnionid;

    /**
     * 用户头像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 账号状态：0-禁用，1-正常
     */
    @TableField("status")
    private Integer status;

    /**
     * 最后登录IP
     */
    @TableField("login_ip")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @TableField("login_time")
    private LocalDateTime loginTime;

    /**
     * 累计登录次数
     */
    @TableField("login_count")
    private Integer loginCount;

    /**
     * 创建时间（插入时自动填充）
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 修改时间（插入/更新时自动填充）
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 逻辑删除：0-未删除，1-已删除（MyBatis-Plus逻辑删除注解）
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
