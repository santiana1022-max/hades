package fun.hades.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * 白名单配置类（从yml加载，提供路径匹配）
 */
@Component
@ConfigurationProperties(prefix = "security.ignore") // 绑定yml配置
public class IgnoreUrlsConfig {

    // 白名单路径列表（自动注入yml中的security.ignore.urls）
    private List<String> urls;

    // Ant路径匹配器（支持通配符**、*、?，企业级标准匹配方式）
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 判断当前请求是否在白名单中
     *
     * @param requestUri 请求路径（如/api/sys/login）
     * @return true-白名单，false-需要校验Token
     */
    public boolean isIgnoreUrl(String requestUri) {
        for (String ignoreUrl : urls) {
            if (PATH_MATCHER.match(ignoreUrl, requestUri)) {
                return true;
            }
        }
        return false;
    }

    // Getter & Setter（必须有，否则配置无法注入）
    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
