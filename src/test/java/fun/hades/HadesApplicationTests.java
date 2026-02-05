package fun.hades;

import fun.hades.common.util.PasswordUtil;
import fun.hades.entity.SysUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class HadesApplicationTests {

    @Autowired
    private PasswordUtil passwordUtil;


    @Test
    void getPassword(){
        SysUser sysUser = new SysUser();
        sysUser.setPassword("123456");

        System.out.printf(passwordUtil.registerUser(sysUser));
    }

    @Test
    public void createToken(){
        // 计算过期时间：当前时间 + 100年（可根据需求调整，比如1000年）
        long expireTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(36500); // 36500天≈100年
        Date expirationDate = new Date(expireTime);

        Map<String, Object> claims = new HashMap<>();
        // 可添加自定义载荷（如用户ID、角色，禁止存敏感信息）
        claims.put("username", "admin");
        String token = Jwts.builder()
                .setClaims(claims) // 载荷
                .setSubject("admin") // 主题（用户名）
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(expirationDate) // 过期时间
                .signWith(Keys.hmacShaKeyFor("hades-TestPlftom-secret-santiana-20260202".getBytes()), SignatureAlgorithm.HS256) // 签名算法+密钥
                .compact();
        log.info("token=========== {}",token);
    }

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
