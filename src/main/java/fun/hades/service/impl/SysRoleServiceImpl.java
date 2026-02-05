package fun.hades.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.hades.entity.SysRole;
import fun.hades.mapper.SysRoleMapper;
import fun.hades.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 角色Service实现
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    @Override
    public List<String> listRoleCodesByUserId(Long userId) {
        return sysRoleMapper.listRoleCodesByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleByIds(List<Long> ids) {
        // 逻辑删除角色
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRole::getId, ids);
        return this.remove(wrapper);
    }
}