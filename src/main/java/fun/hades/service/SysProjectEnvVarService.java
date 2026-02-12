package fun.hades.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.hades.entity.SysProjectEnvVar;

import java.util.List;

/**
 * 环境变量Service接口
 */
public interface SysProjectEnvVarService extends IService<SysProjectEnvVar> {
    /**
     * 根据环境ID查询变量列表
     */
    List<SysProjectEnvVar> selectByEnvId(Long envId);
}
