package fun.hades.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统菜单实体类
 * 对应数据库表：sys_menu
 */
@Data
@TableName("sys_menu")
public class SysMenu {

    /**
     * 主键ID（雪花算法）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID（0表示顶级目录）
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 菜单类型：C-目录，M-菜单，B-按钮
     */
    @TableField("menu_type")
    private String menuType;

    /**
     * 路由路径（前端路由，如：/system/user）
     */
    @TableField("path")
    private String path;

    /**
     * 组件路径（前端组件，如：system/user/index）
     */
    @TableField("component")
    private String component;

    /**
     * 权限标识（如：system:user:list、system:user:add，用于接口/按钮鉴权）
     */
    @TableField("perms")
    private String perms;

    /**
     * 排序号（升序）
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 菜单图标（Element Plus 图标名）
     */
    @TableField("icon")
    private String icon;

    /**
     * 状态：0-禁用，1-正常
     */
    @TableField("status")
    private Integer status;

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
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    // ===================== 非数据库字段 =====================
    /**
     * 子菜单列表（树形结构专用）
     */
    @TableField(exist = false)
    private List<SysMenu> children;
}