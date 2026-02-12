package fun.hades.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.hades.entity.SysProjectModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模块Mapper接口
 */
@Mapper
public interface SysProjectModuleMapper extends BaseMapper<SysProjectModule> {
    /**
     * 根据项目ID查询模块列表
     */
    List<SysProjectModule> selectByProjectId(@Param("projectId") Long projectId);
}
