package fun.hades.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录请求参数DTO（JSON格式）
 */
@Data
public class LoginDTO {

    /**
     * 账号（用户名/手机号/邮箱，三合一）
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码（明文）
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
