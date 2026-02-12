package fun.hades.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.hades.entity.SysProjectEnv;
import fun.hades.mapper.SysProjectEnvMapper;
import fun.hades.service.SysProjectEnvService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 环境Service实现类
 */
@Service
public class SysProjectEnvServiceImpl extends ServiceImpl<SysProjectEnvMapper, SysProjectEnv> implements SysProjectEnvService {

    @Override
    public List<SysProjectEnv> selectByProjectId(Long projectId) {
        return baseMapper.selectByProjectId(projectId);
    }
}
