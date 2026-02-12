package fun.hades.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.hades.entity.SysProjectEnvVar;
import fun.hades.mapper.SysProjectEnvVarMapper;
import fun.hades.service.SysProjectEnvVarService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 环境变量Service实现类
 */
@Service
public class SysProjectEnvVarServiceImpl extends ServiceImpl<SysProjectEnvVarMapper, SysProjectEnvVar> implements SysProjectEnvVarService {

    @Override
    public List<SysProjectEnvVar> selectByEnvId(Long envId) {
        return baseMapper.selectByEnvId(envId);
    }
}
