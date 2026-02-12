package fun.hades.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.hades.entity.SysProject;
import fun.hades.mapper.SysProjectMapper;
import fun.hades.service.SysProjectService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 项目Service实现类
 */
@Service
public class SysProjectServiceImpl extends ServiceImpl<SysProjectMapper, SysProject> implements SysProjectService {

    @Override
    public List<SysProject> selectList(SysProject sysProject) {
        LambdaQueryWrapper<SysProject> queryWrapper = new LambdaQueryWrapper<>();
        // 排序：按创建时间降序
        queryWrapper.orderByDesc(SysProject::getCreateTime);
        // 逻辑删除过滤（MyBatis-Plus自动处理，此处可省略）
        queryWrapper.eq(SysProject::getIsDeleted, 0);
        return baseMapper.selectList(queryWrapper);
    }
}
