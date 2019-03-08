package com.threathunter.web.manager.mysql.mapper;

import com.threathunter.web.manager.mysql.domain.Trunk;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wanbaowang on 17/9/19.
 */
public interface TrunkMapper {

    public void insertOrUpdate(Trunk trunk);

    public List<Trunk> list();

    public void batchDelete(@Param("trunks") List<String> trunks);

}
