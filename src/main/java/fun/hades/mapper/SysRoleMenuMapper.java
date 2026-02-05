package fun.hades.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.hades.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 角色-菜单关联Mapper
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    /**
     * 批量插入角色-菜单关联
     */
    int batchInsert(@Param("list") List<SysRoleMenu> list);
}