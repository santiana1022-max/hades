package fun.hades.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.hades.entity.SysMenu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 菜单Mapper
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 根据角色ID列表查询菜单列表
     */
    List<SysMenu> listMenusByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据角色ID列表查询权限标识列表
     */
    List<String> listPermsByRoleIds(@Param("roleIds") List<Long> roleIds);
}