package fun.hades;

import fun.hades.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class HadesApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("hello java");
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void registerUser() {
        // 明文密码加密
        String encodedPassword = passwordEncoder.encode("tdx.7626");

        System.out.println(encodedPassword);
//        user.setPassword(encodedPassword);
//        sysUserMapper.insert(user);
    }

}
