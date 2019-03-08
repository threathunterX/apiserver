package com.threathunter.web.http.constant;

import com.threathunter.web.common.config.ConfigUtil;

public interface HttpConstant {

    /**
     * Name of the attribute that contains the path
     * within the handler mapping, in case of a pattern match, or the full
     * relevant URI (typically within the DispatcherServlet's mapping) else.
     * <p>Note: This attribute is not required to be supported by all
     * HttpConstant implementations. URL-based HttpConstants will
     * typically support it, but handlers should not necessarily expect
     * this request attribute to be present in all scenarios.
     */
    String PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE = HttpConstant.class.getName() + ".pathWithinHttpConstant";

    /**
     * Name of the attribute that contains the
     * best matching pattern within the handler mapping.
     * <p>Note: This attribute is not required to be supported by all
     * HttpConstant implementations. URL-based HttpConstants will
     * typically support it, but handlers should not necessarily expect
     * this request attribute to be present in all scenarios.
     */
    String BEST_MATCHING_PATTERN_ATTRIBUTE = HttpConstant.class.getName() + ".bestMatchingPattern";

    /**
     * Name of the boolean attribute that indicates
     * whether type-level mappings should be inspected.
     * <p>Note: This attribute is not required to be supported by all
     * HttpConstant implementations.
     */
    String INTROSPECT_TYPE_LEVEL_MAPPING = HttpConstant.class.getName() + ".introspectTypeLevelMapping";

    /**
     * Name of the attribute that contains the URI
     * templates map, mapping variable names to values.
     * <p>Note: This attribute is not required to be supported by all
     * HttpConstant implementations. URL-based HttpConstants will
     * typically support it, but handlers should not necessarily expect
     * this request attribute to be present in all scenarios.
     */
    String URI_TEMPLATE_VARIABLES_ATTRIBUTE = HttpConstant.class.getName() + ".uriTemplateVariables";

    /**
     * Name of the attribute that contains a map with
     * URI matrix variables.
     * <p>Note: This attribute is not required to be supported by all
     * HttpConstant implementations and may also not be present depending on
     * whether the HttpConstant is configured to keep matrix variable content
     * in the request URI.
     */
    String MATRIX_VARIABLES_ATTRIBUTE = HttpConstant.class.getName() + ".matrixVariables";

    /**
     * Name of the attribute that contains the set of
     * producible MediaTypes applicable to the mapped handler.
     * <p>Note: This attribute is not required to be supported by all
     * HttpConstant implementations. Handlers should not necessarily expect
     * this request attribute to be present in all scenarios.
     */
    String PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE = HttpConstant.class.getName() + ".producibleMediaTypes";

    /**
     * 启动spring配置文件地址
     */
//    String HTTP_CONFIG_LOCATION = PropertyUtils.getString("swift.config.location", "resources/spring-swift.xml");

    String HTTP_CONFIG_LOCATION = ConfigUtil.getString("web-http.spring.location");
    /**
     * 链接超时时间
     */
    int HTTP_TIMEOUT = ConfigUtil.getInt("http.timeout");
    /**
     * 请求体最大值
     */
    int HTTP_MAX_CONTENT_LENGTH = ConfigUtil.getInt("http.maxContentLength");
    /**
     * 分发线程数，如果只有一个端口，建议设置成1
     */
    int HTTP_BOSS_THREADS = ConfigUtil.getInt("http.bossThreads");
    /**
     * 工作线程数，如果设置成0，默认为cpu个数 * 2
     */
//    int HTTP_WORKER_THREADS = 2;
    int HTTP_WORKER_THREADS = ConfigUtil.getInt("http.workerThreads");
}
