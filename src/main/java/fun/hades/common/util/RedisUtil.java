package fun.hades.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类：专注 Token 相关操作
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Redis Key 前缀（区分业务）
    private final String LOGIN_TOKEN_PREFIX = "login:token:";

    /**
     * 存储登录 Token
     * @param token 完整的 Bearer Token
     * @param username 用户名
     * @param expire 过期时间（秒）
     */
    public void setLoginToken(String token, String username, long expire) {
        String key = LOGIN_TOKEN_PREFIX + token;
        try {
            // 3. 存储数据（核心代码）
            redisTemplate.opsForValue().set(key, username, expire, TimeUnit.SECONDS);
//            System.out.println("Redis 存储 Token 成功：key={}, username={}, expire={}s", key, username, expire);
        } catch (Exception e) {
            // 4. 捕获 Redis 所有异常，仅打印日志，不抛出（避免影响登录）
            System.out.println(e);
        }
    }

    /**
     * 校验 Token 是否存在（有效）
     * @param token 完整的 Bearer Token
     * @return true-存在有效，false-不存在/已失效
     */
    public boolean hasLoginToken(String token) {
        String key = LOGIN_TOKEN_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除 Token（登出/强制下线时调用）
     * @param token 完整的 Bearer Token
     */
    public void deleteLoginToken(String token) {
        String key = LOGIN_TOKEN_PREFIX + token;
        redisTemplate.delete(key);
    }
}
