package fun.hades.controller;

import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.dto.response.SysUserDTO;
import fun.hades.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * 用户Controller（对外提供用户查询接口）
 */
@RestController
@RequestMapping("/api/user")
@Validated // 开启参数校验
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据用户名查询用户信息
     * @param username 用户名（非空）
     * @return 统一返回结果
     */
    @GetMapping("/getByUsername")
    public Result<SysUserDTO> getByUsername(
            @RequestParam @NotBlank(message = "用户名不能为空") String username
    ) {
        SysUserDTO userDTO = sysUserService.getByUsername(username);
        if (userDTO == null) {
            return Result.error(StatusCodeEnum.USER_NOT_FOUND);
        }
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,userDTO);
    }

    /**
     * 根据手机号查询用户信息
     * @param phone 手机号（非空）
     * @return 统一返回结果
     */
    @GetMapping("/getByPhone")
    public Result<SysUserDTO> getByPhone(
            @RequestParam @NotBlank(message = "手机号不能为空") String phone
    ) {
        SysUserDTO userDTO = sysUserService.getByPhone(phone);
        if (userDTO == null) {
            return Result.error(StatusCodeEnum.USER_NOT_FOUND);
        }
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,userDTO);
    }

    /**
     * 根据邮箱查询用户信息
     * @param email 邮箱（非空）
     * @return 统一返回结果
     */
    @GetMapping("/getByEmail")
    public Result<SysUserDTO> getByEmail(
            @RequestParam @NotBlank(message = "邮箱不能为空") String email
    ) {
        SysUserDTO userDTO = sysUserService.getByEmail(email);
        if (userDTO == null) {
            return Result.error(StatusCodeEnum.USER_NOT_FOUND);
        }
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,userDTO);
    }
}
