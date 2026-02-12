package fun.hades.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.hades.entity.SysProjectModule;
import fun.hades.mapper.SysProjectModuleMapper;
import fun.hades.service.SysProjectModuleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模块Service实现类
 */
@Service
public class SysProjectModuleServiceImpl extends ServiceImpl<SysProjectModuleMapper, SysProjectModule> implements SysProjectModuleService {

    @Override
    public List<SysProjectModule> selectByProjectId(Long projectId) {
        return baseMapper.selectByProjectId(projectId);
    }
}
