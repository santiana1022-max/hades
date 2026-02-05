package fun.hades.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.hades.entity.SysRoleMenu;
import java.util.List;

/**
 * 角色-菜单关联Service接口
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {
    /**
     * 给角色分配菜单
     */
    boolean assignMenuToRole(Long roleId, List<Long> menuIds);

    // 根据角色ID集合，查询关联的菜单ID（去重）
    List<Long> listMenuIdsByRoleIds(List<Long> roleIds);
}