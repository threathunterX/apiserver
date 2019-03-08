package com.threathunter.web.manager.mysql.domain;

import lombok.Data;

@Data
public class StrategyCust {
    private Long id;
    private String app;
    private String name;
    private String remark;
    private String version;
    private String status;
    private Long createtime;
    private Long modifytime;
    private Long starteffect;
    private Long endeffect;
    private Long last_modified;
    private String config;
    private Long score;
    private String tags;
    private Long islock;
    private String category;
    private Long group_id;
}
