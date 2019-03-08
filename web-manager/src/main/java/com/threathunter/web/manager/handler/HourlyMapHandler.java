package com.threathunter.web.manager.handler;

import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 * <p>
 * <p>
 * FROM:
 * variable like:
 * global__order_submit_h5_count__hourly__profile={17=98, 19=98, 20=67},
 * <p>
 * <p>
 * TO:
 * produce json value like:
 * 返回json格式例子:
 * {
 * "data": {
 * "labels": ["00:00", "02:00", "04:00", "06:00", "08:00", "10:00", "12:00", "14:00",'16:00',"18:00","20:00","22:00"],
 * "datasets": [{
 * "label": "订单数",
 * "data": [300, 160, 100, 200, 250, 210, 150, 10, 320, 180, 100, 30]
 * }
 * , {
 * "label": "支付数",
 * "data": [80, 200, 240, 100, 10, 80, 90, 60, 220, 320, 180, 10]
 * }
 * ]
 * }
 * }
 * <p>
 * 设定时间table，默认输出所有时间：
 */
public class HourlyMapHandler extends ValueHandler implements InitializingBean {
    String[] hourTable;

    @Override
    public void handle(String var, Object value, Map<String, Object> result) {
        Object ret = execute(var, value);
        if (ret == null) {
            if (next != null) {
                next.handle(var, value, result);
            }
        } else {
            if (result.get("hourly") == null) {
                Map<String, Object> outerMap = new HashMap<>();
                Map<String, Object> innerMap = new HashMap<>();
                outerMap.put("data", innerMap);
                innerMap.put("labels", hourTable);
                List<Map<String, Object>> dataSets = new LinkedList<>();
                dataSets.add((Map) ret);
                innerMap.put("datasets", dataSets);
                //init hourly map
            } else {
                Map<String, Object> outerMap = (Map<String, Object>) result.get("hourly");
                Map<String, Object> innerMap = (Map<String, Object>) outerMap.get("data");
                List<Map<String, Object>> datasets = (List<Map<String, Object>>) innerMap.get("datasets");
                datasets.add((Map) ret);
            }
        }
    }

    @Override
    public Object execute(String var, Object value) {
        if (var.contains("hourly")) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", var);
            map.put("data", transferValue(value));
            return map;
        }
        return null;
    }

    private Integer[] transferValue(Object value) {
        Map<String, Object> map = (Map<String, Object>) value;
        Integer[] ints = new Integer[24];
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object kValue = entry.getValue();
            int index = Integer.valueOf(key);
            if (index < 0 || index > 23)
                throw new IllegalArgumentException("24小时");
            ints[index] = (Integer) kValue;
        }
        return ints;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        hourTable = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
                "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
                "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    }
}
