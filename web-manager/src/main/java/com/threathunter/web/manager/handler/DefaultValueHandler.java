package com.threathunter.web.manager.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 * <p>
 * FROM:
 * variable like:
 * global__order_submit_h5_count__hourly__profile={17=98, 19=98, 20=67},
 * global__order_submit_h5_sum__1d__profile=263,
 * <p>
 * TO:
 * produce json value like:
 * 返回json格式例子:
 * {
 * "data": {
 * "count": "4953/2201"
 * }
 * }
 */
public class DefaultValueHandler extends ValueHandler {
    @Override
    public Object execute(String var, Object value) {
        Map<String, Object> outMap = new HashMap<>();
        Map<String, Object> innerMap = new HashMap<>();
        outMap.put("data", innerMap);
        innerMap.put(var, value);
        return outMap;
    }
}
