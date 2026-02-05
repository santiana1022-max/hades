package fun.hades.common.util;

import fun.hades.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(SysUser user) {
        // 明文密码加密
        return passwordEncoder.encode(user.getPassword());
    }
}
