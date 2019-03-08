package com.threathunter.web.manager.mysql.mapper;

import com.threathunter.web.manager.mysql.domain.StrategyCust;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public interface StrategyCustMapper {

    void insert(StrategyCust StrategyCust);

    List<StrategyCust> getAll();

    StrategyCust getById(Integer id);

    void delete(Integer id);

    void update(StrategyCust StrategyCust);

    List<String> selectName(@Param("tags") List<String> tags);
}
