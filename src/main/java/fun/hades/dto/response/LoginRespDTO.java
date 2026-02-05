package fun.hades.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录返回结果
 */
@Data
@AllArgsConstructor
public class LoginRespDTO {
    /**
     * 认证令牌（前端携带此Token请求后续接口）
     */
    private String token;
}
