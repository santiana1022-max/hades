package fun.hades.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.hades.entity.SysMenu;
import java.util.List;

/**
 * 菜单Service接口
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 查询菜单树形列表
     */
    List<SysMenu> listMenuTree();


    /**
     * 根据用户ID查询菜单树形列表
     */
    List<SysMenu> listMenuTreeByUserId(Long userId);

    /**
     * 根据用户ID查询权限标识列表
     */
    List<String> listPermsByUserId(Long userId);

    /**
     * 批量删除菜单（逻辑删除）
     */
    boolean deleteMenuByIds(List<Long> ids);

    /*
        通过菜单id查询所有权限标识
     */
    List<String> listPermsByMenuIds(List<Long> menuIds);
}