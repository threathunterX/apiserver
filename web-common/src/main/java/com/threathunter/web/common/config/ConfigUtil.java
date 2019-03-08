package com.threathunter.web.common.config;

import com.threathunter.config.CommonDynamicConfig;

/**
 * Created by wanbaowang on 17/9/18.
 */
public class ConfigUtil {
    public static CommonDynamicConfig getConfig() {
        return ConfigUtilHolder.getConfig();
    }

    public static String getString(String key) {
        CommonDynamicConfig config = ConfigUtilHolder.getConfig();
        return config.getString(key);
    }

    public static String getString(String key, String defaultValue) {
        CommonDynamicConfig config = ConfigUtilHolder.getConfig();
        return config.getString(key, defaultValue);
    }

    public static boolean contains(String key) {
        CommonDynamicConfig config = ConfigUtilHolder.getConfig();
        return config.containsKey(key);
    }

    public static int getInt(String key) {
        CommonDynamicConfig config = ConfigUtilHolder.getConfig();
        return config.getInt(key);
    }

    private static class ConfigUtilHolder {
        private static CommonDynamicConfig commonDynamicConfig = CommonDynamicConfig.getInstance();

        static {
            commonDynamicConfig.addConfigFiles("nebula.conf", "java-web.properties");
        }

        public static CommonDynamicConfig getConfig() {
            return commonDynamicConfig;
        }
    }
}
