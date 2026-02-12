package fun.hades.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.hades.common.Result;
import fun.hades.common.enums.StatusCodeEnum;
import fun.hades.entity.SysProject;
import fun.hades.service.SysProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目控制器
 */
@RestController
@RequestMapping("/sys/project")
public class SysProjectController {

    @Autowired
    private SysProjectService sysProjectService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:project:list')")
    public Result<List<SysProject>> selectList(SysProject sysProject) {
        List<SysProject> list = sysProjectService.selectList(sysProject);
        return Result.success(StatusCodeEnum.SUCCESS_GENERAL,list);
    }

}
