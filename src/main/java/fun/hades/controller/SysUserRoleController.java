package fun.hades.controller;

import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 用户-角色关联接口
 */
@RestController
@RequestMapping("/api/sys/userRole")
@RequiredArgsConstructor
public class SysUserRoleController {

    private final SysUserRoleService sysUserRoleService;

    /**
     * 给用户分配角色
     */
    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('system:user:assignRole')")
    public Result<Boolean> assign(
            @RequestParam Long userId,
            @RequestBody List<Long> roleIds) {
        boolean result = sysUserRoleService.assignRoleToUser(userId, roleIds);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,result);
    }
}