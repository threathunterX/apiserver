package com.threathunter.web.manager.service;

import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import java.util.*;
import org.slf4j.*;

@Service
public class AlarmService
{
    private static final Logger log;
    @Autowired
    MysqlService mysqlService;
    
    public List<Map<String, Object>> getStatistic(final Long startTime, final Long endTime) {
        final Map<Long, Integer> statisticTest = this.mysqlService.selectStaticCount(startTime, endTime, 1);
        final Map<Long, Integer> statisticProd = this.mysqlService.selectStaticCount(startTime, endTime, 0);
        final List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        for (long l = startTime; l <= endTime; l += 3600000L) {
            final Map<String, Object> m = new HashMap<String, Object>();
            m.put("production_count", statisticProd.getOrDefault(l, 0));
            m.put("time_frame", l);
            m.put("test_count", statisticTest.getOrDefault(l, 0));
            ret.add(m);
        }
        return ret;
    }
    
    static {
        log = LoggerFactory.getLogger((Class)AlarmService.class);
    }
}
