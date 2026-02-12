package fun.hades.controller;

import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.entity.SysMenu;
import fun.hades.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 菜单管理接口
 */
@Slf4j
@RestController
@RequestMapping("/sys/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    // 当前登录用户可访问的菜单树（前端导航栏专用）
    @GetMapping("/user/menu")
    @PreAuthorize("isAuthenticated()")
    public Result<List<SysMenu>> getUserMenu() {
        List<SysMenu> userMenuTree = sysMenuService.getUserMenuTree();
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL, userMenuTree);
    }
}