package fun.hades.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.dto.SysUserQueryDTO;
import fun.hades.dto.response.SysUserDTO;
import fun.hades.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 用户Controller（对外提供用户查询接口）
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated // 开启参数校验
public class SysUserController {

    private final SysUserService sysUserService;

    /**
     * 分页查询用户列表 isAuthenticated()  登录后即可访问
     */
    @GetMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public Result<SysUserDTO> list(){
        SysUserDTO sysUserDTO = sysUserService.getUserInfo();
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,sysUserDTO);
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result<Page<SysUserDTO>> list(@Validated @RequestBody SysUserQueryDTO queryDTO){
        Page<SysUserDTO> userPage = sysUserService.getUserPage(queryDTO);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,userPage);
    }



}
