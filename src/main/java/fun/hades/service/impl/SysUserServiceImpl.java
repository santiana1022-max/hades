package fun.hades.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.hades.dto.SysUserQueryDTO;
import fun.hades.dto.response.SysRoleDTO;
import fun.hades.dto.response.SysUserDTO;
import fun.hades.entity.SysUser;
import fun.hades.entity.convert.SysUserConvert;
import fun.hades.mapper.SysUserMapper;
import fun.hades.mapper.SysUserRoleMapper;
import fun.hades.service.SysUserRoleService;
import fun.hades.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {


    private final SysUserMapper sysUserMapper;

    private final SysUserRoleService sysUserRoleService;

    /*
        获取用户信息
     */
    @Override
    public SysUserDTO getUserInfo() {

        return null;
    }

    @Override
    public Page<SysUserDTO> getUserPage(SysUserQueryDTO sysUserQueryDTO) {
        Page<SysUser> page = new Page<>(sysUserQueryDTO.getPageNum(), sysUserQueryDTO.getPageSize());
        IPage<SysUser> sysUserIPage = sysUserMapper.selectUserPage(page, sysUserQueryDTO.getUsername(), sysUserQueryDTO.getPhone(), sysUserQueryDTO.getStatus());

        List<SysUserDTO> dtoList = SysUserConvert.INSTANCE.convertToDTOList(sysUserIPage.getRecords());

        // 3. 多对多核心：批量查询角色，组装到DTO
        if (CollectionUtils.isNotEmpty(dtoList)) {
            // 提取所有用户ID
            List<Long> userIds = dtoList.stream().map(SysUserDTO::getId).collect(Collectors.toList());
            // 批量查询：用户ID -> 角色列表
            Map<Long, List<SysRoleDTO>> userRoleMap = sysUserRoleService.listRoleMapByUserIds(userIds);
            // 为每个用户设置角色列表（无角色则返回空列表，非null）
            dtoList.forEach(dto -> {
                List<SysRoleDTO> roleList = userRoleMap.getOrDefault(dto.getId(), Collections.emptyList());
                dto.setRoleList(roleList);
            });
        }

        Page<SysUserDTO> dtoPage = new Page<>();
        dtoPage.setRecords(dtoList); // 转换后的DTO列表
        dtoPage.setTotal(sysUserIPage.getTotal()); // 总记录数
        dtoPage.setSize(sysUserIPage.getSize()); // 每页条数
        dtoPage.setCurrent(sysUserIPage.getCurrent()); // 当前页
        dtoPage.setPages(sysUserIPage.getPages()); // 总页数


        return dtoPage;
    }






    /**
     * 通用方法：根据账号（用户名/手机号/邮箱）查询用户
     */
    public SysUser getUserByAccount(String account) {
        // 按优先级查询：用户名 > 手机号 > 邮箱
        SysUser user = sysUserMapper.selectByUsername(account);
        if (user == null) {
            user = sysUserMapper.selectByPhone(account);
        }
        if (user == null) {
            user = sysUserMapper.selectByEmail(account);
        }
        return user;
    }
}
