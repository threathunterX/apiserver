package com.threathunter.web.manager.mysql.mapper;

import com.threathunter.web.manager.mysql.domain.LogQuery;

import java.util.List;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public interface LogQueryMapper {

    void insert(LogQuery query);

    List<LogQuery> getAll();

    LogQuery getById(Integer id);

    void delete(Integer id);

    void update(LogQuery query);

    List<Integer> getAllNonSuccessIds();

    List<Integer> getAllSuccessIds();
}
