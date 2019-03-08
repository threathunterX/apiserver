package com.threathunter.web.manager.mysql.domain;

import lombok.Data;

import java.sql.Timestamp;


/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Data
public class Porter {
    private int id;
    private String name;
    private String remark;
    private long count;
    private Timestamp create_time;
    private Timestamp modify_time;
    private String schema;
    private String key;
    private int status;
}
