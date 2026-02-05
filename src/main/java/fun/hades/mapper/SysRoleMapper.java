package fun.hades.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.hades.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 角色Mapper
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据用户ID查询角色编码列表
     */
    List<String> listRoleCodesByUserId(@Param("userId") Long userId);
}