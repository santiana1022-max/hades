package fun.hades.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.hades.entity.SysProject;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目Mapper接口
 */
@Mapper
public interface SysProjectMapper extends BaseMapper<SysProject> {
    // 基础CRUD由MyBatis-Plus自动实现，自定义方法可在此扩展
}
