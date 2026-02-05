package fun.hades.common.exception;

import fun.hades.common.enums.StatusCodeEnum;

/**
 * 自定义业务异常（企业级标准：用于抛出业务逻辑错误）
 * 区别于系统异常（如 NullPointerException、SQLException），专门处理业务校验失败
 */
public class BusinessException extends RuntimeException {

    /**
     * 序列化ID（企业级必备，保证异常序列化兼容）
     */
    private static final long serialVersionUID = 1L;

    /**
     * 业务错误码（可选，企业级常用，用于前端精准判断错误类型）
     */
    private Integer code;

    /**
     * 错误提示信息（必选，返回给前端的友好提示）
     */
    private String msg;

    // ==================== 核心构造方法（覆盖常用业务场景） ====================

    /**
     * 仅传递错误信息（默认错误码 500，适配全局返回体）
     * @param msg 错误提示
     */
    public BusinessException(StatusCodeEnum statusCodeEnum) {
        super(statusCodeEnum.getMsg());
        this.code = statusCodeEnum.getCode();
        this.msg = statusCodeEnum.getMsg();

    }

    /**
     * 传递错误码 + 错误信息（细粒度控制错误类型）
     * @param code 错误码
     * @param msg 错误提示
     */
    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 传递错误信息 + 原始异常（保留异常堆栈，方便排查问题）
     * @param msg 错误提示
     * @param cause 原始异常
     */
    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
        this.code = 500;
    }

    // ==================== Getter 方法（全局异常处理器读取异常信息用） ====================

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}