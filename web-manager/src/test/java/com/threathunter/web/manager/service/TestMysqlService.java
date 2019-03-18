package com.threathunter.web.manager.service;

import com.threathunter.web.manager.mysql.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:spring/spring-all.xml"})
@Slf4j
public class TestMysqlService extends TestAbstractService {

    public static void main(String args) {
        TestMysqlService service = new TestMysqlService();
    }

    @Ignore
    @Test
    public void testBatchDelete() {
        List<String> trunkUrls = Arrays.asList("http://www.baidu.com", "http://bg.com/url@item9");
        this.mysqlService.batchDelete(trunkUrls);
    }

    @Ignore
    @Test
    public void testNotice() {
        Notice notice = this.mysqlService.getNotice();
        System.out.println(notice.getId());
    }

    @Ignore
    @Test
    public void testPorters() {
        log.debug("start to test porters from mysql");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        Porter porter = new Porter();
        porter.setName("name1");
        porter.setCount(123);
        porter.setKey("key123");
        porter.setRemark("remark123123123213");
        porter.setSchema("schema123123");
        porter.setStatus(1);
        /**/
        mysqlService.insertPorter(porter);
        sqlSession.commit();
        log.debug(String.format("porter : %s", porter));
        Porter porter1 = mysqlService.queryPorter(51);
        log.debug(String.format("porter.create time:%s, modify time:%s", porter1.getCreate_time(), porter1.getModify_time()));
        assertThat(porter1).isNotNull();

    }

    @Test
    public void testMysql_getLogquery() {
        log.debug("start to test query from mysql");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);

//        log.debug(String.format("porter : %s", porter));/**/
        LogQuery logQuey = mysqlService.queryLogQuery(1);
     /**/
        log.debug(String.format("porter.create time:%s, LogQuery:%s", logQuey.getCreateTime(), logQuey.toString()));
        assertThat(logQuey).isNotNull();

    }

    @Test
    public void testMysql_insertLogQuery() {
        log.debug("start to test query_crud from mysql");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        LogQuery query = new LogQuery();
        query.setCreateTime(System.currentTimeMillis());
        query.setDownloadPath("1231231231313");
        query.setEndTime(System.currentTimeMillis() + 1000L);
        query.setError("errorrorororor");
        query.setEventName("eventevent");
        query.setFromTime(System.currentTimeMillis());
        query.setRemark("remarkremark");
        query.setStatus(LogQueryStatus.SUCCESS);
        mysqlService.insertLogQuery(query);
        assertThat(query).isNotNull();
        assertThat(query.getId()).isNotEqualTo(0);
        query.setRemark("yyyyyyyyyyyyyyyyyyyy");
        mysqlService.updateLogQuery(query);
        assertThat(mysqlService.queryLogQuery(query.getId())).isEqualTo(query);
    }

    @Test
    public void testMysql_deleteLogQuery() {
        log.debug("======start to delete query from mysql=====");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        LogQuery logQuery = mysqlService.queryLogQuery(4);
        assertThat(logQuery).isNotNull();
        mysqlService.deleteLogQuery(4);
        logQuery = mysqlService.queryLogQuery(4);
        assertThat(logQuery).isNull();
    }

    @Test
    public void testMysql() {
        List<String> params = new ArrayList<>();
        params.add("str1");
        params.add("str2");
        params.add("str3");
        params.add("str4");
        params.add("str5");
        params.add("str6");
        StringBuilder str = new StringBuilder(params.toString());
        System.out.println(str.toString());
    }

    @Test
    public void testMySql_getAllSlotNotice() {
        log.debug("======start to delete query from mysql=====");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        List<SlotNotice> allSlotNotice = mysqlService.querySlotNotice();
        assertThat(allSlotNotice).isNotNull();
        assertThat(allSlotNotice.size()).isNotEqualTo(0);
    }


    @Test
    public void testMySql_getSlotNoticeStatic() {
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        Map<Long, Integer> staticCountFromSlotNotice = mysqlService.selectStaticCount(1508842800000L, 1516794144987L, 0);
        for (Map.Entry<Long, Integer> entry : staticCountFromSlotNotice.entrySet()) {
            System.out.println(String.format("ts:%s", entry.getKey()));
            System.out.println(String.format("value:%s", entry.getValue()));
        }
        assertThat(staticCountFromSlotNotice.size()).isNotEqualTo(0);
    }

    @Test
    public void testMySql_filterAggregated() {
        /**
         * id=2701,
         * timestamp=1517203757814,
         * key=172.16.10.110,
         * strategy_name=IP相同UA大量请求单个页面,
         * scene_name=VISITOR,
         * checkpoints=,*
         * check_type=IP,*
         * decision=review,*
         * risk_score=0,*
         * expire=1517204057814,*
         * remark=>50, *
         * 1 页面, *
         * 1UA, *
         * last_modified=null,*
         * variable_values=null,*
         * geo_province=内网, *
         * geo_city=内网, *
         * test=1,*
         * tip=IP相同UA大量请求单个页面, *
         * uri_stem=wiki.threathunter.net/rest/mywork/latest/task, *
         * trigger_event=null
         */
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        SlotNoticeService.Criteria criteria = new SlotNoticeService.Criteria();
        criteria.setTest(1L);
        criteria.setCheckTypes(Arrays.asList(new String[]{"IP"}));
        criteria.setKey("172.16.10.110");
        criteria.setStrategies(Arrays.asList(new String[]{"IP相同UA大量请求单个页面"}));
        List<SlotNotice> slotNotices = mysqlService.querySlotNoticeAtAggregate(criteria, 20, 20);
        assertThat(slotNotices.size()).isNotEqualTo(0);
    }
}
