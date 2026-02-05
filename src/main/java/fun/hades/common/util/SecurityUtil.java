package fun.hades.common.util;

import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.common.exception.BusinessException;
import fun.hades.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security 上下文工具类（企业级标准：静态方法，全局复用）
 */
public class SecurityUtil {

    /**
     * 获取当前登录用户ID（核心方法，全项目通用）
     */
    public static Long getCurrentUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取当前登录用户名
     */
    public static String getCurrentUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 内部方法：获取完整的 LoginUser
     */
    private static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BusinessException(StatusCodeEnum.USER_NOT_LOING);
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof LoginUser)) {
            throw new BusinessException(StatusCodeEnum.REN_ZHENG_XIN_XI);
        }

        return (LoginUser) principal;
    }
}