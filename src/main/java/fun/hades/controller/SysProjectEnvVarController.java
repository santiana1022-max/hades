package fun.hades.controller;

import fun.hades.common.Result;
import fun.hades.entity.SysProjectEnvVar;
import fun.hades.service.SysProjectEnvVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 环境变量控制器
 */
@RestController
@RequestMapping("/project/env/var")
public class SysProjectEnvVarController {

    @Autowired
    private SysProjectEnvVarService sysProjectEnvVarService;

}
