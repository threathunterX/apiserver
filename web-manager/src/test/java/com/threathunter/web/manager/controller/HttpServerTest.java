package com.threathunter.web.manager.controller;

import com.threathunter.web.manager.HttpBootstrap;
import org.junit.Test;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public class HttpServerTest {
    @Test
    public void testServer() {
        HttpBootstrap.main(null);
    }
}
