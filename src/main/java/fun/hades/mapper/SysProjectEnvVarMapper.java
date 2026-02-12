package fun.hades.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.hades.entity.SysProjectEnvVar;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 环境变量Mapper接口
 */
@Mapper
public interface SysProjectEnvVarMapper extends BaseMapper<SysProjectEnvVar> {
    /**
     * 根据环境ID查询变量列表
     */
    List<SysProjectEnvVar> selectByEnvId(@Param("envId") Long envId);
}
