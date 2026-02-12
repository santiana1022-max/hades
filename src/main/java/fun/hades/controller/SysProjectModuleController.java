package fun.hades.controller;

import fun.hades.common.Result;
import fun.hades.entity.SysProjectModule;
import fun.hades.service.SysProjectModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模块控制器
 */
@RestController
@RequestMapping("/project/module")
public class SysProjectModuleController {

    @Autowired
    private SysProjectModuleService sysProjectModuleService;

}
