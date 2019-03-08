package com.threathunter.web.manager.service;

import com.threathunter.config.CommonDynamicConfig;
import com.threathunter.metrics.MetricsAgent;
import com.threathunter.metrics.model.LegendData;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public class TestMetricAgent {

    @BeforeClass
    public static void setUp() {
        CommonDynamicConfig.getInstance().addConfigFile("nebula.conf");
        MetricsAgent.getInstance().start();
    }

    @Test
    public void testMetricAgent_dataDiskUsage() {
        System.out.println("=================testMetricAgent_dataDiskUsage=================");
        Long current = System.currentTimeMillis();
        Long pre = current - 60 * 1000L;
        List<LegendData> rows = MetricsAgent.getInstance().query("monitor", "systemStats.dataDiskUsage", "mean", pre, current, 60, null, "hostip");
//        List<LegendData> rows = MetricsAgent.getInstance().query("webui", "metrics_proxy_test", "mean", 1453717794955l, 1453718160946l, 10, null, "name");
        if (rows != null) {
            for (LegendData row : rows) {
                System.out.println(row.toString());
            }
        } else {
            System.out.println("null data");
        }
    }

    @Test
    public void testMetricAgent_usedMemory() {
        System.out.println("=================testMetricAgent_usedMemory=================");
        Long current = System.currentTimeMillis();
        Long pre = current - 5 * 60 * 1000L;
        List<LegendData> rows = MetricsAgent.getInstance().query("monitor", "systemStats.usedMemory", "mean", pre, current, 30, null, "hostip");
//        List<LegendData> rows = MetricsAgent.getInstance().query("webui", "metrics_proxy_test", "mean", 1453717794955l, 1453718160946l, 10, null, "name");
        if (rows != null) {
            for (LegendData row : rows) {
                System.out.println(row.toString());
            }
        } else {
            System.out.println("null data");
        }
    }

    @Test
    public void testMetricAgent_cpuLoad() {
        System.out.println("=================testMetricAgent_cpuLoad=================");
        Long current = System.currentTimeMillis();
        Long pre = current - 5 * 60 * 1000L;
        List<LegendData> rows = MetricsAgent.getInstance().query("monitor", "systemStats.cpuLoad", "max", pre, current, 30, null, "hostip");
        if (rows != null) {
            for (LegendData row : rows) {
                System.out.println(row.toString());
            }
        } else {
            System.out.println("null data");
        }
    }

    //获取1小时的数据，5分钟聚合，一共12个点，metrics取sum
    @Test
    public void testMetricAgent_statistics() {
        System.out.println("=================testMetricAgent_statistics=================");
        Long current = System.currentTimeMillis();
        Long pre = current - 60 * 60 * 1000L;
        List<LegendData> rows = MetricsAgent.getInstance().query("nebula.online", "events.income.count", "sum", pre, current, 5 * 60, null, "hostip");
        if (rows != null) {
            for (LegendData row : rows) {
                System.out.println(row.toString());
            }
        } else {
            System.out.println("null data");
        }
    }
}
