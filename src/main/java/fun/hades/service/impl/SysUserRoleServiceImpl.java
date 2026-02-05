package fun.hades.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.hades.dto.response.SysRoleDTO;
import fun.hades.entity.SysRole;
import fun.hades.entity.SysUserRole;
import fun.hades.mapper.SysRoleMapper;
import fun.hades.mapper.SysUserRoleMapper;
import fun.hades.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户-角色关联Service实现
 */
@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    private final SysUserRoleMapper sysUserRoleMapper;

    private final SysRoleMapper sysRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoleToUser(Long userId, List<Long> roleIds) {
        // 1. 删除原有关联
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        this.remove(wrapper);

        // 2. 批量新增关联
        if (roleIds == null || roleIds.isEmpty()) {
            return true;
        }
        List<SysUserRole> list = roleIds.stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreateBy("system");
            return userRole;
        }).collect(Collectors.toList());
        return sysUserRoleMapper.batchInsert(list) > 0;
    }

    /**
     * 批量查询用户角色（多对多，一次性查询，无N+1）
     */
    @Override
    public Map<Long, List<SysRoleDTO>> listRoleMapByUserIds(List<Long> userIds) {
        // 空值处理
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }

        // 1. 查询中间表：用户-角色关联关系（过滤逻辑删除）
        List<SysUserRole> userRoleList = this.lambdaQuery()
                .in(SysUserRole::getUserId, userIds)
                .eq(SysUserRole::getIsDeleted, 0)
                .list();
        if (CollectionUtils.isEmpty(userRoleList)) {
            return Collections.emptyMap();
        }

        // 2. 批量查询角色详情（避免循环查角色表）
        List<Long> roleIds = userRoleList.stream()
                .map(SysUserRole::getRoleId)
                .distinct()
                .collect(Collectors.toList());
        List<SysRole> roleList = sysRoleMapper.selectBatchIds(roleIds);

        // 3. 构建 角色ID -> 角色DTO 映射
        Map<Long, SysRoleDTO> roleMap = roleList.stream()
                .collect(Collectors.toMap(
                        SysRole::getId,
                        role -> {
                            SysRoleDTO dto = new SysRoleDTO();
                            dto.setId(role.getId());
                            dto.setRoleName(role.getRoleName());
                            dto.setRoleCode(role.getRoleCode());
                            return dto;
                        }
                ));

        // 4. 构建 用户ID -> 角色列表 映射（多对多核心）
        return userRoleList.stream()
                .collect(Collectors.groupingBy(
                        SysUserRole::getUserId,
                        Collectors.mapping(ur -> roleMap.get(ur.getRoleId()), Collectors.toList())
                ));
    }
}