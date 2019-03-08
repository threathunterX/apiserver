package com.threathunter.web.manager.mysql.mapper;

import com.threathunter.web.manager.mysql.domain.*;
import java.util.*;
import org.apache.ibatis.annotations.*;

public interface SlotNoticeMapper
{
    void insert(final SlotNotice p0);
    
    List<SlotNotice> getAll();
    
    SlotNotice getById(final Integer p0);
    
    void delete(final Integer p0);
    
    void update(final SlotNotice p0);
    
    int count();
    
    List getStaticCount(@Param("startTime") final Long p0, @Param("endTime") final Long p1, @Param("test") final int p2);
    
    List<SlotNotice> filterByExpireAndDecision(@Param("expire") final Long p0, @Param("decision") final String p1);
    
    List<SlotNotice> filterAggregate(@Param("filterExpire") final boolean p0, @Param("decisions") final List<String> p1, @Param("strategies") final List<String> p2, @Param("checkTypes") final List<String> p3, @Param("sceneTypes") final List<String> p4, @Param("fromTime") final Long p5, @Param("endTime") final Long p6, @Param("key") final String p7, @Param("test") final Long p8, @Param("offset") final int p9, @Param("rowCount") final int p10);
    
    List<SlotNotice> filterNonAggregate(@Param("filterExpire") final boolean p0, @Param("decisions") final List<String> p1, @Param("strategies") final List<String> p2, @Param("checkTypes") final List<String> p3, @Param("sceneTypes") final List<String> p4, @Param("fromTime") final Long p5, @Param("endTime") final Long p6, @Param("key") final String p7, @Param("test") final Long p8, @Param("offset") final int p9, @Param("rowCount") final int p10);
    
    Integer selectCountAtAggregate(@Param("filterExpire") final boolean p0, @Param("decisions") final List<String> p1, @Param("strategies") final List<String> p2, @Param("checkTypes") final List<String> p3, @Param("sceneTypes") final List<String> p4, @Param("fromTime") final Long p5, @Param("endTime") final Long p6, @Param("key") final String p7, @Param("test") final Long p8);
    
    Integer selectCountAtNonAggregate(@Param("filterExpire") final boolean p0, @Param("decisions") final List<String> p1, @Param("strategies") final List<String> p2, @Param("checkTypes") final List<String> p3, @Param("sceneTypes") final List<String> p4, @Param("fromTime") final Long p5, @Param("endTime") final Long p6, @Param("key") final String p7, @Param("test") final Long p8);
    
    List<Long> selectIdByExpireAndDecision();
    
    void insertList(@Param("notices") final List<SlotNotice> p0);
    
    void deleteByKeyAndTime(@Param("key") final String p0, @Param("fromtime") final Long p1, @Param("endtime") final Long p2);
}
