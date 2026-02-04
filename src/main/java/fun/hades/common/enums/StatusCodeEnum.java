package fun.hades.common.enums;

/**
 * 全局错误码枚举（集中管理所有错误信息，解耦业务代码）
 * 错误码规范：1000开头=通用错误，2000开头=用户模块错误，3000开头=系统模块错误
 */
public enum StatusCodeEnum {

    // 成功提示
    SUCCESS(1000, "请求成功"),

    // 通用错误
    PARAM_ERROR(1001, "参数错误"),
    UNAUTHORIZED(1002, "未授权，请先登录"),
    FORBIDDEN(1003, "无权限访问"),
    SERVER_ERROR(1004, "服务器繁忙，请稍后再试"),

    // 用户模块
    // 2用户模块错误
    ACCOUNT_PASSWORD_ERROR(2001, "账号或密码错误"),
    USER_NOT_FOUND(2002, "用户不存在"),
    USER_DISABLED(2003, "账号已被禁用，请联系管理员"),
    PHONE_NOT_EXIST(2004, "手机号未注册"),

    //  系统模块错误（可扩展）
    IP_LIMIT(3001, "IP访问频繁，请稍后再试"),
    FILE_UPLOAD_ERROR(3002, "文件上传失败");

    // 错误码
    private final int code;
    // 错误文案
    private final String msg;

    // 构造方法
    StatusCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // getter 方法
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
