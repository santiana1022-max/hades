package fun.hades.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 项目环境变量表实体
 */
@Data
@TableName("sys_project_env_var")
public class SysProjectEnvVar {
    /** 变量ID（自增主键） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属环境ID */
    @TableField("env_id")
    private Long envId;

    /** 变量名称 */
    @TableField("var_name")
    private String varName;

    /** 变量值（加密存储） */
    @TableField("var_value")
    private String varValue;

    /** 变量类型 */
    @TableField("var_type")
    private String varType;

    /** 加密类型 */
    @TableField("encrypt_type")
    private String encryptType;

    /** 变量描述 */
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
