package fun.hades.controller;

import fun.hades.common.Result;
import fun.hades.entity.SysProjectEnv;
import fun.hades.service.SysProjectEnvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 环境控制器
 */
@RestController
@RequestMapping("/project/env")
public class SysProjectEnvController {

    @Autowired
    private SysProjectEnvService sysProjectEnvService;

}
