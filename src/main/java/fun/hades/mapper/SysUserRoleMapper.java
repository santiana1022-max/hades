package fun.hades.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.hades.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户-角色关联Mapper
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 根据用户ID查询角色ID列表
     */
    List<Long> listRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 批量插入用户-角色关联
     */
    int batchInsert(@Param("list") List<SysUserRole> list);
}