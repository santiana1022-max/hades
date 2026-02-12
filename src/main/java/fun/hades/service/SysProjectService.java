package fun.hades.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.hades.entity.SysProject;

import java.util.List;

/**
 * 项目Service接口
 */
public interface SysProjectService extends IService<SysProject> {

    /*
        查询所有项目
     */
    List<SysProject> selectList(SysProject sysProject);

}
