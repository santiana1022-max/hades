package fun.hades.common.util;

import fun.hades.entity.SysMenu;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单树形结构构建工具
 */
public class MenuTreeUtil {
    public static List<SysMenu> buildTree(List<SysMenu> menuList) {
        // 过滤禁用/删除菜单
        List<SysMenu> validMenus = menuList.stream()
                .filter(menu -> 1 == menu.getStatus() && 0 == menu.getIsDeleted())
                .collect(Collectors.toList());

        // 按父ID分组
        Map<Long, List<SysMenu>> parentMap = validMenus.stream()
                .collect(Collectors.groupingBy(SysMenu::getParentId));

        // 填充子菜单
        validMenus.forEach(menu -> {
            List<SysMenu> children = parentMap.getOrDefault(menu.getId(), new ArrayList<>());
            children.sort((a, b) -> a.getSort() - b.getSort());
            menu.setChildren(children);
        });

        // 返回顶级菜单
        return validMenus.stream()
                .filter(menu -> 0 == menu.getParentId())
                .sorted((a, b) -> a.getSort() - b.getSort())
                .collect(Collectors.toList());
    }
}