package fun.hades.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.hades.common.util.MenuTreeUtil;
import fun.hades.common.util.RedisUtil;
import fun.hades.entity.SysMenu;
import fun.hades.mapper.SysMenuMapper;
import fun.hades.mapper.SysUserRoleMapper;
import fun.hades.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜单Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final RedisUtil redisUtil;

    // 缓存前缀
    private static final String MENU_CACHE_PREFIX = "menu:user:";
    private static final String PERM_CACHE_PREFIX = "perm:user:";
    private static final long CACHE_EXPIRE = 120L;

    @Override
    public List<SysMenu> listMenuTree() {
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort));
        return MenuTreeUtil.buildTree(menuList);
    }

    @Override
    public List<SysMenu> listMenuTreeByUserId(Long userId) {
        // 缓存查询
//        String cacheKey = MENU_CACHE_PREFIX + userId;
//        List<SysMenu> cacheMenu = (List<SysMenu>) redisUtil.(cacheKey);
//        if (cacheMenu != null) {
//            return cacheMenu;
//        }
        // 数据库查询
        List<Long> roleIds = sysUserRoleMapper.listRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return Arrays.asList();
        }
        List<SysMenu> menuList = sysMenuMapper.listMenusByRoleIds(roleIds);
        List<SysMenu> treeMenu = MenuTreeUtil.buildTree(menuList);
        // 缓存写入
//        redisUtil.set(cacheKey, treeMenu, CACHE_EXPIRE, TimeUnit.MINUTES);
        return treeMenu;
    }

    @Override
    public List<String> listPermsByUserId(Long userId) {
//        String cacheKey = PERM_CACHE_PREFIX + userId;
//        List<String> cachePerms = (List<String>) redisUtil.get(cacheKey);
//        if (cachePerms != null) {
//            return cachePerms;
//        }
        List<Long> roleIds = sysUserRoleMapper.listRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return Arrays.asList();
        }
        List<String> perms = sysMenuMapper.listPermsByRoleIds(roleIds);
//        redisUtil.setLoginToken(cacheKey, perms, CACHE_EXPIRE, TimeUnit.MINUTES);
        return perms;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenuByIds(List<Long> ids) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysMenu::getId, ids).or().in(SysMenu::getParentId, ids);
        return this.remove(wrapper);
    }

    @Override
    public List<String> listPermsByMenuIds(List<Long> menuIds) {
        log.info("接收的菜单ID列表：{}", menuIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Arrays.asList();
        }
        List<String> collect = this.lambdaQuery()
                .in(SysMenu::getId, menuIds)
                .eq(SysMenu::getIsDeleted, 0)
                .eq(SysMenu::getStatus, 1)
                .isNotNull(SysMenu::getPerms)
                .ne(SysMenu::getPerms, "")
                .list()
                .stream()
                .map(SysMenu::getPerms)
                .distinct() // 去重（同一权限可能对应多个菜单）
                .collect(Collectors.toList());
        return collect;
    }

    // 【新增】获取当前用户可访问的菜单树
    @Override
    public List<SysMenu> getUserMenuTree() {
        // 1. 获取当前登录用户的权限标识列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new ArrayList<>();
        }
        // 转换权限：GrantedAuthority -> 权限字符串（如 sys:menu:list）
        List<String> userPerms = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        log.info("userPerms =========> {}",userPerms);

        if (CollectionUtils.isEmpty(userPerms)) {
            return new ArrayList<>();
        }

        // 2. 构造查询条件：只查用户有权限、正常状态、非按钮的菜单
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(SysMenu::getIsDeleted, 0)           // 未删除
                .eq(SysMenu::getStatus, 1)              // 正常状态
                .ne(SysMenu::getMenuType, "B")         // 排除按钮类型
                // 嵌套条件：(目录类型C) OR (菜单类型M 且 权限在用户列表中)
                .nested(w -> w.eq(SysMenu::getMenuType, "C")
                        .or()
                        .eq(SysMenu::getMenuType, "M")
                        .in(SysMenu::getPerms, userPerms));

        // 3. 查询符合条件的菜单列表
        List<SysMenu> menuList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }

        log.info("menuList ======================> {}", menuList);

        // 4. 构建树形结构（根节点 parent_id=0，递归找子节点）
        return buildMenuTree(menuList, 0L);
    }

    // 【通用】构建菜单树形结构（递归方法）
    private List<SysMenu> buildMenuTree(List<SysMenu> menuList, Long parentId) {
        List<SysMenu> treeList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            // 找到当前父节点的子菜单
            if (parentId.equals(menu.getParentId())) {
                // 递归查找子菜单的子节点
                List<SysMenu> children = buildMenuTree(menuList, menu.getId());
                menu.setChildren(children); // 设置子菜单
                treeList.add(menu);
            }
        }
        // 按 sort 字段升序排序（保证菜单顺序正确）
        treeList.sort((a, b) -> a.getSort() - b.getSort());
        return treeList;
    }
}