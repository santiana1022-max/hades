package fun.hades.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 项目环境配置表实体
 */
@Data
@TableName("sys_project_env")
public class SysProjectEnv {
    /** 环境ID（自增主键） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属项目ID */
    @TableField("project_id")
    private Long projectId;

    /** 环境名称 */
    @TableField("env_name")
    private String envName;

    /** 环境编码 */
    @TableField("env_code")
    private String envCode;

    /** 环境状态：0-禁用，1-启用 */
    @TableField("status")
    private Integer status;

    /** 环境描述 */
    @TableField("description")
    private String description;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 创建人 */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 修改人 */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 逻辑删除 */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
