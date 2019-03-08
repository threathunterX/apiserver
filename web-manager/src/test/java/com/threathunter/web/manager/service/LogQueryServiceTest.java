package com.threathunter.web.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@ContextConfiguration(value = {"classpath:spring/spring-all.xml"})
public class LogQueryServiceTest extends TestAbstractService {
    @Test
    public void testLogQueryService_downloadAtBase64() {
        String s = logQueryService.downloadAtBase64("events_query_1");
        System.out.println(s);

    }
}
