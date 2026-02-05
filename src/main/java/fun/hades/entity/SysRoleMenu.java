package fun.hades.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色-菜单关联实体类
 * 对应数据库表：sys_role_menu
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu {

    /**
     * 主键ID（雪花算法）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色ID（关联sys_role.id）
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 菜单ID（关联sys_menu.id）
     */
    @TableField("menu_id")
    private Long menuId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}