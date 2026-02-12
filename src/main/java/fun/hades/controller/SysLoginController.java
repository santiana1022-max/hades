package fun.hades.controller;

import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.common.util.JwtUtil;
import fun.hades.common.util.RedisUtil;
import fun.hades.dto.response.LoginRespDTO;
import fun.hades.dto.response.SysUserDTO;
import fun.hades.entity.convert.SysUserConvert;
import fun.hades.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/sys")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SysLoginController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final SysUserService sysUserService;

    private final RedisUtil redisUtil;

    /**
     * 登录接口（返回JWT令牌）
     */
    @PostMapping("/login")
    public Result<LoginRespDTO> login(@RequestBody Map<String, String> loginParam, HttpServletRequest request) {
        /*
            todo：
                1.登录接口限流防刷
                2.账号密码错误次数限制 + 临时锁定
                3.图形验证码 / 短信验证码
                4.登录态增强：Refresh Token 机制（替代单纯的自动续签
                5.登录日志持久化
                6.登出功能增强：Token 黑名单 + Redis 清理
                7.推荐功能：
                    7.1.多设备登录管理
                    7.2.异地登录检测 + 告警
                    7.3.记住我（免密登录）
                    7.4.密码安全增强
                    7.5.防止表单重复提交
                    7.6.第三方登录(微信登录)
                    7.7。手机号一键登录

         */

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
            log.info("userDetails: {}",userDetails.getAuthorities());

            // 4. 生成JWT令牌
            String token = jwtUtil.generateToken(userDetails);
            String fullToken = jwtUtil.getPrefix() + token; // 拼接前缀

            // 4. 【核心】Token 存入 Redis，过期时间与 JWT 一致（7200秒）
            redisUtil.setLoginToken(fullToken, account, jwtUtil.getExpire() / 1000);

            return Result.success(StatusCodeEnum.SUCCESS_LOGIN, new LoginRespDTO(fullToken));

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
        return Result.success(StatusCodeEnum.SUCCESS_LOGOUT);
    }
}
