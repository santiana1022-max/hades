package fun.hades.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 项目信息表实体
 */
@Data
@TableName("sys_project")
public class SysProject {
    /** 项目ID（自增主键） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 项目名称 */
    @TableField("project_name")
    private String projectName;

    /** 项目编码 */
    @TableField("project_code")
    private String projectCode;

    /** 项目负责人ID */
    @TableField("owner_user_id")
    private Long ownerUserId;

    /** 项目成员ID列表（逗号分隔） */
    @TableField("member_user_ids")
    private String memberUserIds;

    /** 项目状态：0-禁用，1-启用 */
    @TableField("status")
    private Integer status;

    /** 项目描述 */
    @TableField("description")
    private String description;

    /** 创建时间（自动填充） */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 创建人 */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /** 更新时间（自动填充） */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 修改人 */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}