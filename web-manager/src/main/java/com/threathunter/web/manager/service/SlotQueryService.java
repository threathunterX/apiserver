package com.threathunter.web.manager.service;

import com.threathunter.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Slf4j
@Service
public class SlotQueryService {
    static long HOUR_MILLIS = 1000 * 60 * 60L;
    @Autowired
    BabelService babelService;

    public Map<String, Object> query(Map<String, Object> map) {
        Long ts = ((Double) map.get("timestamp")).longValue();
        Event event = null;
        if (isCurrentHour(ts)) {
            event = babelService.onlineKeyStatQuery(map);
        } else {
            event = babelService.offlineKeyStatQuery(map);

        }
        if (event != null) {
        Map<String, Object> ret = event.getPropertyValues();
            ret = (Map<String, Object>) ret.get("result");
            //解包：key __GLOBAL__
            Object global = ret.get("__GLOBAL__");
            if (global instanceof Map) {
                ret.remove("__GLOBAL__");
                ret = (Map<String, Object>) global;
            }
            return ret;
        } else
            return new HashMap<>();
    }

    private boolean isCurrentHour(Long ts) {

        Long cur = System.currentTimeMillis();
        Long cur_time = cur / HOUR_MILLIS * HOUR_MILLIS;
        Long ts_time = ts / HOUR_MILLIS * HOUR_MILLIS;
        if (cur_time.compareTo(ts_time) == 0)
            return true;
        return false;
    }
}
