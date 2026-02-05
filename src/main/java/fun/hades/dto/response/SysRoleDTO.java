package fun.hades.dto.response;

import lombok.Data;

/**
 * 角色传输对象（前端展示用）
 */
@Data
public class SysRoleDTO {
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码（权限标识用）
     */
    private String roleCode;
}
