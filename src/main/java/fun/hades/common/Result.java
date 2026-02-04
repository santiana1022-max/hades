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
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(StatusCodeEnum.SUCCESS.getCode());
        result.setMsg(StatusCodeEnum.SUCCESS.getMsg());
        return result;
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(StatusCodeEnum.SUCCESS.getCode());
        result.setMsg(StatusCodeEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    // 2. 错误响应（核心：基于枚举的方法，推荐使用）
    public static <T> Result<T> error(StatusCodeEnum errorCode) {
        Result<T> result = new Result<>();
        result.setCode(errorCode.getCode());
        result.setMsg(errorCode.getMsg());
        return result;
    }

    // 链式调用（可选，提升代码简洁性）
    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
