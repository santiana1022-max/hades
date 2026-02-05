package fun.hades.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.hades.dto.response.SysRoleDTO;
import fun.hades.entity.SysUserRole;
import java.util.List;
import java.util.Map;

/**
 * 用户-角色关联Service接口
 */
public interface SysUserRoleService extends IService<SysUserRole> {
    /**
     * 给用户分配角色
     */
    boolean assignRoleToUser(Long userId, List<Long> roleIds);


    List<Long> listRoleIdsByUserId(Long userId);

    /**
     * 查询用户的角色列表
     * @param userId 用户ID集合
     * @return key=用户ID，value=该用户的角色DTO列表
     */
    List<SysRoleDTO> listRoleMapByUserId(Long userId);

    /**
     * 批量查询用户的角色列表
     * @param userIds 用户ID集合
     * @return key=用户ID，value=该用户的角色DTO列表
     */
    Map<Long, List<SysRoleDTO>> listRoleMapByUserIds(List<Long> userIds);
}