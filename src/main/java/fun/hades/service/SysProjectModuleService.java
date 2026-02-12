package fun.hades.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.hades.entity.SysProjectModule;

import java.util.List;

/**
 * 模块Service接口
 */
public interface SysProjectModuleService extends IService<SysProjectModule> {
    /**
     * 根据项目ID查询模块列表
     */
    List<SysProjectModule> selectByProjectId(Long projectId);
}
