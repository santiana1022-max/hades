package fun.hades.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 项目模块表实体
 */
@Data
@TableName("sys_project_module")
public class SysProjectModule {
    /** 模块ID（自增主键） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属项目ID */
    @TableField("project_id")
    private Long projectId;

    /** 模块名称 */
    @TableField("module_name")
    private String moduleName;

    /** 模块编码 */
    @TableField("module_code")
    private String moduleCode;

    /** 排序号 */
    @TableField("sort")
    private Integer sort;

    /** 模块描述 */
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
