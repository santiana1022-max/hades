package fun.hades.common.util;


import javax.servlet.http.HttpServletRequest;

/**
 * Web通用工具类（全局复用，无业务耦合）
 */
public class WebUtil {

    /**
     * 工具方法：获取客户端真实IP（兼容代理、Nginx场景）
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多代理情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    // TODO:后续可添加其他Web工具方法：如获取请求头、获取User-Agent等
}