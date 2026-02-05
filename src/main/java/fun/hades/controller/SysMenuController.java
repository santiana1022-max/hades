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
@RequestMapping("/api/sys/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    /**
     * 查询菜单树形列表
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result<List<SysMenu>> tree() {
        // 【临时调试】打印当前用户的权限列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("当前用户权限：{}", authentication.getAuthorities()); // 看这里是否有 sys:menu:list

        List<SysMenu> menuTree = sysMenuService.listMenuTree();
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,menuTree);
    }

    /**
     * 获取当前用户菜单树形列表
     */
    @GetMapping("/userMenu")
    public Result<List<SysMenu>> userMenu() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SysMenu> menuTree = sysMenuService.listMenuTreeByUserId(userId);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,menuTree);
    }

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:menu:add')")
    public Result<Boolean> add(@Validated @RequestBody SysMenu menu) {
        boolean result = sysMenuService.save(menu);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,result);
    }

    /**
     * 修改菜单
     */
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Boolean> edit(@Validated @RequestBody SysMenu menu) {
        boolean result = sysMenuService.updateById(menu);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,result);
    }

    /**
     * 批量删除菜单
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        boolean result = sysMenuService.deleteMenuByIds(ids);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,result);
    }
}