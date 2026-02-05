package fun.hades.entity.convert;

import fun.hades.entity.SysUser;
import fun.hades.dto.response.SysUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * 用户对象转换器（MapStruct 自动生成实现类）
 */
@Mapper
public interface SysUserConvert {
    // 单例实例
    SysUserConvert INSTANCE = Mappers.getMapper(SysUserConvert.class);

    /**
     * SysUser 转 SysUserDTO（单个对象）
     */
    SysUserDTO convertToDTO(SysUser user);

    /**
     * SysUser 列表 转 SysUserDTO 列表
     */
    List<SysUserDTO> convertToDTOList(List<SysUser> userList);
}