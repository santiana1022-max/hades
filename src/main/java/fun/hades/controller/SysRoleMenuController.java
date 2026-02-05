package fun.hades.controller;

import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 角色-菜单关联接口
 */
@RestController
@RequestMapping("/api/sys/roleMenu")
@RequiredArgsConstructor
public class SysRoleMenuController {

    private final SysRoleMenuService sysRoleMenuService;

    /**
     * 给角色分配菜单
     */
    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('system:role:assignPerm')")
    public Result<Boolean> assign(
            @RequestParam Long roleId,
            @RequestBody List<Long> menuIds) {
        boolean result = sysRoleMenuService.assignMenuToRole(roleId, menuIds);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,result);
    }
}