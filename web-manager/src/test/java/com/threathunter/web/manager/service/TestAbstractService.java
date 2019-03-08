package com.threathunter.web.manager.service;

import com.threathunter.config.CommonDynamicConfig;
import com.threathunter.metrics.MetricsAgent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by wanbaowang on 17/10/10.
 */
public abstract class TestAbstractService implements ApplicationContextAware{
   @Autowired   protected RedisService redisService;
    @Autowired   protected MysqlService mysqlService;
    @Autowired
    protected LogQueryService logQueryService;
    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext =applicationContext;
    }

    static {
        System.out.println("======================");
        CommonDynamicConfig.getInstance().addConfigFile("nebula.conf");

        CommonDynamicConfig.getInstance().addOverrideProperty("platform.persistent.query.dir", "/home/yy/query");
        MetricsAgent.getInstance().start();
    }

    /*  public void init() {
//        this.applicationContext = new ClassPathXmlApplicationContext(HttpConstant.HTTP_CONFIG_LOCATION);
        this.applicationContext = new ClassPathXmlApplicationContext("spring/spring-all.xml");
//        this.applicationContext = new ClassPathXmlApplicationContext("spring/spring-mysql.xml","spring/spring-java-http.xml","spring/spring-redis.xml","spring/spring-rpc-client.xml");
        this.redisService = this.applicationContext.getBean(RedisService.class);
        this.mysqlService = this.applicationContext.getBean(MysqlService.class);*/
}
