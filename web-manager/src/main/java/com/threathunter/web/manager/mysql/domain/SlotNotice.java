package com.threathunter.web.manager.mysql.domain;

import lombok.Data;

import java.util.Map;

@Data
public class SlotNotice {
    private Long id;
    private Long timestamp;
    private String key;
    private String strategy_name;
    private String scene_name;
    private String checkpoints;
    private String check_type;
    private String decision;
    private Long risk_score;
    private Long expire;
    private String remark;
    private Long last_modified;
    private Map<String, Object> variable_values;
    private String geo_province;
    private String geo_city;
    private Long test;
    private String tip;
    private String uri_stem;
    private Map<String, Object> trigger_event;
    private boolean aggregated;
    private Integer count;
    private boolean key_in_whitelist;
    private Map<String, Object> whitelist;

}
