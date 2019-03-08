package com.threathunter.web.manager.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 * <p>
 * <p>
 * FROM:
 * variable like:
 * global_product_location__order_submit_h5_count__1d__profile={北京=25, 上海=20, 杭州=11},
 * <p>
 * <p>
 * TO:
 * produce json value like:
 * Top城市（风险订单）
 * url:/...
 * 返回json格式例子：
 * {
 * "data": {
 * "rank": [{
 * "label": "beijing",
 * "value": 78
 * }, {
 * "label": "shanghai",
 * "value": 69
 * }, {
 * "label": "guangzhou",
 * "value": 58
 * }, {
 * "label": "wuhan",
 * "value": 40
 * }, {
 * "label": "chengdu",
 * "value": 35
 * }, {
 * "label": "tinajing",
 * "value": 10
 * }]
 * }
 * }
 */
public class SortMapHandler extends ValueHandler {
    @Override
    public Object execute(String var, Object value) {
        if (var.contains("order")) {
            Map<String, Object> outerMap = new HashMap<>();
            Map<String, Object> innerMap = new HashMap<>();
            outerMap.put("data", innerMap);
            List<Map<String, Object>> items = new ArrayList<>();
            innerMap.put("rank", items);
            Map<String, Object> values = (Map) value;
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                Map<String, Object> m = new HashMap<>();
                m.put("label", entry.getKey());
                m.put("value", entry.getValue());
                items.add(m);
            }
            return outerMap;
        }
        return null;
    }
}
