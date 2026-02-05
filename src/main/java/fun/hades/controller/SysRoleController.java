package fun.hades.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.entity.SysRole;
import fun.hades.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 角色管理接口
 */
@RestController
@RequestMapping("/api/sys/role")
@RequiredArgsConstructor
@Validated
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<IPage<SysRole>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String roleCode) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        IPage<SysRole> rolePage = sysRoleService.page(page);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,rolePage);
    }

    /**
     * 根据ID查询角色详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<SysRole> getById(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,role);
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:role:add')")
    public Result<Boolean> add(@Validated @RequestBody SysRole role) {
        boolean result = sysRoleService.save(role);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,result);
    }

    /**
     * 修改角色
     */
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Boolean> edit(@Validated @RequestBody SysRole role) {
        boolean result = sysRoleService.updateById(role);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,result);
    }

    /**
     * 批量删除角色
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        boolean result = sysRoleService.deleteRoleByIds(ids);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,result);
    }
}