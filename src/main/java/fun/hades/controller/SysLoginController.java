package fun.hades.controller;

import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.common.util.JwtUtil;
import fun.hades.common.util.RedisUtil;
import fun.hades.dto.response.SysUserDTO;
import fun.hades.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/api/sys")
public class SysLoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录接口（返回JWT令牌）
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginParam, HttpServletRequest request) {
        String account = loginParam.get("account");
        String password = loginParam.get("password");

        // 1. 参数校验（用枚举返回错误）
        if (account == null || account.isEmpty() || password == null || password.isEmpty()) {
            return Result.error(StatusCodeEnum.PARAM_ERROR);
        }

        try {
            // 2. 验证用户名密码（Spring Security认证）
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(account, password)
            );

            // 3. 加载用户信息
            UserDetails userDetails = userDetailsService.loadUserByUsername(account);

            // 4. 生成JWT令牌
            String token = jwtUtil.generateToken(userDetails);
            String fullToken = jwtUtil.getPrefix() + token; // 拼接前缀
            SysUserDTO userDTO = SysUserDTO.convertToDTO(sysUserService.getUserByAccount(account));

            // 4. 【核心】Token 存入 Redis，过期时间与 JWT 一致（7200秒）
            redisUtil.setLoginToken(fullToken, account, jwtUtil.getExpire() / 1000);

            // 5. 封装返回数据（令牌+用户信息）
            Map<String, Object> resultData = new LinkedHashMap<>();
            resultData.put("token", fullToken);

            resultData.put("user", userDTO);

            // 6. 成功响应（用你统一的枚举文案）
            return Result.success(resultData);

        } catch (Exception e) {
            // 7. 认证失败（用枚举返回错误）
            return Result.error(StatusCodeEnum.ACCOUNT_PASSWORD_ERROR);
        }
    }

    /**
     * 登出接口：删除 Redis 中的 Token，实现主动失效
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        // 从请求头获取 Token
        String token = request.getHeader(jwtUtil.getHeader());
        if (token == null || !token.startsWith(jwtUtil.getPrefix())) {
            return Result.error(StatusCodeEnum.PARAM_ERROR);
        }

        // 【核心】删除 Redis 中的 Token
        redisUtil.deleteLoginToken(token);
        return Result.success("登出成功");
    }
}
