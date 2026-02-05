package fun.hades.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.hades.common.util.MenuTreeUtil;
import fun.hades.common.util.RedisUtil;
import fun.hades.entity.SysMenu;
import fun.hades.mapper.SysMenuMapper;
import fun.hades.mapper.SysUserRoleMapper;
import fun.hades.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 菜单Service实现
 */
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
}