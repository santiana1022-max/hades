package fun.hades.dto;

import lombok.Data;

/**
 * 用户查询 DTO（JSON 传参用）
 */
@Data
public class SysUserQueryDTO {
    // 用户名（模糊查询）
    private String username;
    // 手机号（模糊查询）
    private String phone;
    // 状态：0-禁用，1-正常
    private Integer status;
    // 页码
    private Integer pageNum = 1;
    // 每页条数
    private Integer pageSize = 10;
}