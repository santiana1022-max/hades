package fun.hades.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.hades.entity.SysRole;
import java.util.List;

/**
 * 角色Service接口
 */
public interface SysRoleService extends IService<SysRole> {
    /**
     * 根据用户ID查询角色编码
     */
    List<String> listRoleCodesByUserId(Long userId);

    /**
     * 批量删除角色（逻辑删除）
     */
    boolean deleteRoleByIds(List<Long> ids);
}