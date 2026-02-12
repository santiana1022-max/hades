package fun.hades.common.enums;

/**
 * 全局错误码枚举（集中管理所有错误信息，解耦业务代码）
 * 错误码规范：1000开头=通用错误，2000开头=用户模块错误，3000开头=系统模块错误
 */
public enum StatusCodeEnum {

    // 成功提示
    SUCCESS_GENERAL(200, "请求成功"),
    SUCCESS_LOGIN(200, "登录成功"),
    SUCCESS_LOGOUT(200, "退出成功"),

    // 通用错误
    SERVER_ERROR(1000, "服务器繁忙，请稍后再试"),
    TOKEN_IS_EMPTY(1001, "token为空"),
    TOKEN_NOT_EXIST(1002, "token 不存在，请先登录"),
    TOKEN_EXPIRED(1003, "token 已过期，请重新登录"),
    TOKEN_INVALID(1004, "无效token"),
    PARAM_ERROR(1005, "参数错误"),
    UNAUTHORIZED(1006, "未授权，请先登录"),
    FORBIDDEN(1007, "无权限访问"),


    // 用户模块
    ACCOUNT_PASSWORD_ERROR(1000, "账号或密码错误"),
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_NOT_LOING(1002, "用户未登录，请先登录"),
    REN_ZHENG_XIN_XI(1003, "认证信息异常"),
    USER_DISABLED(1004, "账号已被禁用，请联系管理员"),
    PHONE_NOT_EXIST(1005, "手机号未注册"),

    //  系统模块错误（可扩展）
    SYSTEM_ERROR(500, "系统开小差，请稍后再试"),
    IP_LIMIT(501, "IP访问频繁，请稍后再试"),
    FILE_UPLOAD_ERROR(502, "文件上传失败");


    // 错误码
    private final Integer code;
    // 错误文案
    private final String msg;

    // 构造方法
    StatusCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // getter 方法
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
