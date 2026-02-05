package fun.hades.common;

import fun.hades.common.enums.StatusCodeEnum;
import lombok.Data;

/**
 * 通用返回结果封装
 * @param <T> 数据泛型
 */
@Data
public class Result<T> {
    /**
     * 状态码：200-成功，500-失败
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    // 成功响应（无数据）
    public static <T> Result<T> success(StatusCodeEnum codeEnum) {
        Result<T> result = new Result<>();
        result.setCode(codeEnum.getCode());
        result.setMsg(codeEnum.getMsg());
        return result;
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(StatusCodeEnum codeEnum,T data) {
        Result<T> result = new Result<>();
        result.setCode(codeEnum.getCode());
        result.setMsg(codeEnum.getMsg());
        result.setData(data);
        return result;
    }


    // token异常
    public static <T> Result<T> unauthorized(StatusCodeEnum codeEnum) {
        Result<T> result = new Result<>();
        result.setCode(codeEnum.getCode());
        result.setMsg(codeEnum.getMsg());
        return result;
    }

    // 业务异常
    public static <T> Result<T> fail(StatusCodeEnum codeEnum) {
        Result<T> result = new Result<>();
        result.setCode(codeEnum.getCode());
        result.setMsg(codeEnum.getMsg());
        return result;
    }

    // 服务异常
    public static <T> Result<T> error(StatusCodeEnum codeEnum) {
        Result<T> result = new Result<>();
        result.setCode(codeEnum.getCode());
        result.setMsg(codeEnum.getMsg());
        return result;
    }

    // 链式调用（可选，提升代码简洁性）
    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
