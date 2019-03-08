package com.threathunter.web.manager.mysql.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 * mysql
 * +----+---------------+---------------+---------+--------+-------+---------------+--------------------+---------------+---------------------------------------------------+------------------------------------+
 * | id | fromtime      | endtime       | status  | remark | error | event_name    | download_path      | create_time   | terms                                             | show_cols                          |
 * +----+---------------+---------------+---------+--------+-------+---------------+--------------------+---------------+---------------------------------------------------+------------------------------------+
 * |  1 | 1515393031000 | 1515479435000 | success |        | NULL  | ACCOUNT_LOGIN | events_query_1.csv | 1515479445967 | [{"op": "contain", "right": ".", "left": "c_ip"}] | c_ip,did,sid,page,c_port,timestamp |
 * +----+---------------+---------------+---------+--------+-------+---------------+--------------------+---------------+---------------------------------------------------+------------------------------------+
 * +---------------+-------------------------------------------+------+-----+---------+----------------+
 * | Field         | Type                                      | Null | Key | Default | Extra          |
 * +---------------+-------------------------------------------+------+-----+---------+----------------+
 * | id            | int(11)                                   | NO   | PRI | NULL    | auto_increment |
 * | fromtime      | bigint(20)                                | YES  |     | NULL    |                |
 * | endtime       | bigint(20)                                | YES  |     | NULL    |                |
 * | status        | enum('wait','process','success','failed') | YES  |     | NULL    |                |
 * | remark        | varchar(300)                              | YES  |     | NULL    |                |
 * | error         | varchar(200)                              | YES  |     | NULL    |                |
 * | event_name    | varchar(100)                              | YES  |     | NULL    |                |
 * | download_path | char(100)                                 | YES  |     | NULL    |                |
 * | create_time   | bigint(20)                                | YES  |     | NULL    |                |
 * | terms         | varchar(2000)                             | YES  |     | NULL    |                |
 * | show_cols     | varchar(2000)                             | YES  |     | NULL    |                |
 * +---------------+-------------------------------------------+------+-----+---------+----------------+
 */
@Data
public class LogQuery {
    private int id;
    private Long fromTime;
    private Long endTime;
    private LogQueryStatus status;
    private String remark;
    private String error;
    private String eventName;
    private String downloadPath;
    private Long createTime;
    private List<Map<String, Object>> terms;
    private List<String> showColumns;
}
