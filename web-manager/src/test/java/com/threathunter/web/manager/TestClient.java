package com.threathunter.web.manager;

import com.threathunter.web.manager.rpc.GlobalVariableClient;
import com.threathunter.web.manager.service.TestAbstractService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by wanbaowang on 17/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@ContextConfiguration(value = {"classpath:spring/spring-all.xml"})
public class TestClient extends TestAbstractService {

    @Test
    public void testGlobalVariableQuery() throws Exception {
        GlobalVariableClient globalVariableClient = applicationContext.getBean(GlobalVariableClient.class);
        String day = "20180116";
        List<String> vars = Arrays.asList("global__order_submit_h5_count__hourly__profile", "global_product_location__order_submit_h5_count__hourly__profile",
                "global__order_submit_h5_sum__1d__profile", "global_product_location__order_submit_h5_count__1d__profile");
        Map<String, Object> results = globalVariableClient.query(day, vars);
        System.out.println(results);
    }

}
