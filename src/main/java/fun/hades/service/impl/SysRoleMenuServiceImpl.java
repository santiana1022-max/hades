package fun.hades.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.hades.entity.SysRoleMenu;
import fun.hades.mapper.SysRoleMenuMapper;
import fun.hades.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色-菜单关联Service实现
 */
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenuToRole(Long roleId, List<Long> menuIds) {
        // 1. 删除原有关联
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.remove(wrapper);

        // 2. 批量新增关联
        if (menuIds == null || menuIds.isEmpty()) {
            return true;
        }
        List<SysRoleMenu> list = menuIds.stream().map(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenu.setCreateBy("system");
            return roleMenu;
        }).collect(Collectors.toList());
        return sysRoleMenuMapper.batchInsert(list) > 0;
    }

    @Override
    public List<Long> listMenuIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Arrays.asList();
        }
        return this.lambdaQuery()
                .in(SysRoleMenu::getRoleId, roleIds)
                .eq(SysRoleMenu::getIsDeleted, 0)
                .list()
                .stream()
                .map(SysRoleMenu::getMenuId)
                .distinct() // 去重（避免同一菜单被多个角色关联）
                .collect(Collectors.toList());
    }
}