package com.threathunter.web.manager.mysql.mapper;

import com.threathunter.web.manager.mysql.domain.Porter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public interface PorterMapper {

    void insert(Porter porter);

    List<Porter> getAll();

    Porter getById(@Param("id") Integer id);

    void updateStatus(@Param("id") Integer id);

}
