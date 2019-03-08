package com.threathunter.web.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Service
@Slf4j
public class NetworkStatisticsService {

    @Autowired
    MetricsAgentService service;

    public List<Map<String, Object>> getStatistics() {
        Map<Long, Double> statistics = service.getStatistics();
        List<Map<String, Object>> ret = new ArrayList();
        for (Map.Entry<Long, Double> entry : statistics.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("count", entry.getValue());
            map.put("time_frame", entry.getKey());
            ret.add(map);
        }
        Collections.sort(ret, (map1, map2) -> {
            Long key1 = (Long) map1.get("time_frame");
            Long key2 = (Long) map2.get("time_frame");
            return key1.compareTo(key2);
        });
        return ret;
    }
}
