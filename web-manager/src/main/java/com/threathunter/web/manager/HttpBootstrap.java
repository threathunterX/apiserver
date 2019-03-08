package com.threathunter.web.manager;


import com.threathunter.web.common.config.ConfigUtil;
import com.threathunter.web.common.utils.ObjUtils;
import com.threathunter.web.http.server.HttpServer;

public class HttpBootstrap {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int port = ConfigUtil.getInt("http-manager-port");
        if (args != null && args.length > 0) {
            port = ObjUtils.toInteger(args[0], port);
        }
        new HttpServer(port).start(startTime);
    }
}
