package com.threathunter.web.manager.service;

import org.junit.Test;

/**
 * 
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
