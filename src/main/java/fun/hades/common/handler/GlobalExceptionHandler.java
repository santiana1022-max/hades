package fun.hades.common.handler;

import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice // 全局异常处理注解，自动扫描所有Controller
public class GlobalExceptionHandler {

    // 1. 捕获所有未明确处理的异常（兜底）
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e, HttpServletRequest request) {
        log.error("【全局异常】请求地址：{}，异常信息：", request.getRequestURI(), e);
        return Result.error(StatusCodeEnum.SERVER_ERROR);
    }

    // 2. 捕获空指针异常（可自定义细分）
    @ExceptionHandler(NullPointerException.class)
    public Result<Object> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("【空指针异常】请求地址：{}，异常信息：", request.getRequestURI(), e);
        return Result.error(StatusCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request){
        log.error("登录用户权限不足，需要检查权限,请求地址：{} , 异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.error(StatusCodeEnum.FORBIDDEN);
    }

    // 3. 捕获参数校验异常（如@NotBlank、@NotNull）
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Object> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("【参数异常】请求地址：{}，异常信息：", request.getRequestURI(), e);
        return Result.fail(StatusCodeEnum.PARAM_ERROR);
    }

    // 4. 可扩展：自定义业务异常（如用户不存在、密码错误）
//    @ExceptionHandler(BusinessException.class)
//    public Result<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
//        log.error("【业务异常】请求地址：{}，异常信息：", request.getRequestURI(), e);
//        return Result.fail(e.getCode(), e.getMessage());
//    }
}
