package fun.hades.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

/**
 * JSON 工具类（基于 Spring 内置 Jackson 实现）
 * 提供对象转JSON字符串、JSON字符串转对象的核心方法
 */
public class JSONUtil {

    // 全局复用 ObjectMapper（线程安全，提升性能）
    private static final ObjectMapper OBJECT_MAPPER;

    // 静态初始化：配置 ObjectMapper 核心参数
    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 1. 支持 Java 8 时间类型（LocalDateTime/LocalDate 等）
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        // 2. 关闭时间戳序列化（转为标准日期格式）
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 3. 忽略空对象序列化异常
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    // 私有化构造方法，禁止实例化
    private JSONUtil() {
    }

    /**
     * 对象转 JSON 字符串（核心方法，适配过滤器响应场景）
     * @param obj 待转换的对象（如 ResponseResult）
     * @return JSON 字符串，转换失败返回空字符串
     */
    public static String toJsonStr(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // 打印异常日志，便于排查问题
            e.printStackTrace();
            return "";
        }
    }

    /**
     * JSON 字符串转指定类型对象
     * @param jsonStr JSON 字符串
     * @param clazz 目标对象类型
     * @return 转换后的对象，转换失败返回 null
     * @param <T> 泛型
     */
    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        if (jsonStr == null || jsonStr.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
