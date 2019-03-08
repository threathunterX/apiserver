package com.threathunter.web.manager.service;

import org.junit.Test;

/**
 * Created by wanbaowang on 17/10/10.
 */
public class TestRedisService extends TestAbstractService {

    @Test
    public void testCurrentVersion() {
        System.out.println(this.redisService.getCurrentVersion());
    }

    @Test
    public void testIncrementVersion() {
        System.out.println(this.redisService.incrementVersion());
    }

}
