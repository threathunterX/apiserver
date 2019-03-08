package com.threathunter.web.manager.service;

import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.threathunter.model.*;
import java.util.*;
import org.slf4j.*;

@Service
public class ClickStreamQueryService
{
    private static final Logger log;
    @Autowired
    BabelService babelService;
    
    public Map<String, Object> queryPeriod(final String key, final String dimension, final Long fromTime, final Long endTime) {
        final Event event = this.babelService.getClickQueryDetail(key, dimension, fromTime, endTime, "clicks_period");
        if (event == null) {
            return new HashMap<String, Object>();
        }
        final Map<String, Object> propertyValues = (Map<String, Object>)event.getPropertyValues();
        return (Map<String, Object>)propertyValues.get("clicks_period");
    }
    
    public List<Map<String, Object>> queryVisit(final String key, final String dimension, final Long fromTime, final Long endTime) {
        final Event event = this.babelService.getClickQueryDetail(key, dimension, fromTime, endTime, "visit_stream");
        if (event == null) {
            return new ArrayList<Map<String, Object>>();
        }
        final Map<String, Object> propertyValues = (Map<String, Object>)event.getPropertyValues();
        return (List<Map<String, Object>>)propertyValues.get("visit_stream");
    }
    
    public List<Map<String, Object>> queryClicks(final String key, final String dimension, final Long fromTime, final Long endTime, final List query) {
        final Event event = this.babelService.getClickQueryDetail(key, dimension, fromTime, endTime, "clicks", query);
        if (event == null) {
            return new ArrayList<Map<String, Object>>();
        }
        final Map<String, Object> propertyValues = (Map<String, Object>)event.getPropertyValues();
        return (List<Map<String, Object>>) propertyValues.get("clicks");
    }
    
    static {
        log = LoggerFactory.getLogger(ClickStreamQueryService.class);
    }
}
