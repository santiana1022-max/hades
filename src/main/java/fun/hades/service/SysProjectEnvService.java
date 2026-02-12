package fun.hades.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.hades.entity.SysProjectEnv;

import java.util.List;

/**
 * 环境Service接口
 */
public interface SysProjectEnvService extends IService<SysProjectEnv> {
    /**
     * 根据项目ID查询环境列表
     */
    List<SysProjectEnv> selectByProjectId(Long projectId);
}
