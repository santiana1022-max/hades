package fun.hades.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.hades.entity.SysProjectEnv;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 环境Mapper接口
 */
@Mapper
public interface SysProjectEnvMapper extends BaseMapper<SysProjectEnv> {
    /**
     * 根据项目ID查询环境列表
     */
    List<SysProjectEnv> selectByProjectId(@Param("projectId") Long projectId);
}
