package com.threathunter.web.manager.service;

import com.threathunter.metrics.MetricsAgent;
import com.threathunter.metrics.model.LegendData;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Service
public class MetricsAgentService {
    private static final Logger logger = LoggerFactory.getLogger(MetricsAgentService.class);
    static Long interval = 5 * 60 * 1000L;
    static Long total = 60 * 60 * 1000L;

    static {
        MetricsAgent.getInstance().start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> MetricsAgent.getInstance().stop()));
    }

    public Double getSpace() {
        Long current = System.currentTimeMillis();
        Long pre = current - 60 * 1000L;
        List<LegendData> legendDatas = MetricsAgent.getInstance().query("monitor", "systemStats.dataDiskUsage", "mean", pre, current, 60, null, "hostip");
        if (legendDatas.size() > 0) {
            LegendData legendData = legendDatas.get(0);
            Map<Long, Double> legend = legendData.getTsValues();
            for (Map.Entry<Long, Double> entry : legend.entrySet())
                return entry.getValue();
        }
        return 0.0D;
    }

    public List<Double> getCpu() {
        List<Double> ret = new ArrayList<>();
        Long current = System.currentTimeMillis();
        Long pre = current - 5 * 60 * 1000L;
        List<LegendData> legendDatas = MetricsAgent.getInstance().query("monitor", "systemStats.cpuLoad", "max", pre, current, 30, null, "hostip");
        legendDataToList(legendDatas, ret);
        return ret;
    }

    private void legendDataToList(List<LegendData> legendDatas, List<Double> dest) {
        if (legendDatas != null) {
            for (LegendData data : legendDatas) {
                Map<Long, Double> legend = data.getTsValues();
                for (Double value : legend.values())
                    dest.add(value);
            }
        }
    }

    public List<Double> getMemory() {
        List<Double> ret = new ArrayList<>();
        Long current = System.currentTimeMillis();
        Long pre = current - 5 * 60 * 1000L;
        List<LegendData> legendDatas = MetricsAgent.getInstance().query("monitor", "systemStats.availableMemory", "mean", pre, current, 30, null, "hostip");
        legendDataToList(legendDatas, ret);
        return ret;
    }

    public Map<Long, Double> getStatistics() {
        List<Double> ret = new ArrayList<>();
        Long current = System.currentTimeMillis();
        current = current / interval * interval;
        Long pre = current - 60 * 60 * 1000L;
        List<LegendData> legendDatas = MetricsAgent.getInstance().query("nebula.online", "events.income.count", "sum", pre, current, 5 * 60, null, "hostip");
        for (LegendData data : legendDatas) {
            Map<Long, Double> tsValues = data.getTsValues();
            addTsPointIfZero(pre, current, tsValues);
            return tsValues;
        }
        return new HashMap<Long, Double>();
    }

    private void addTsPointIfZero(Long start, Long end, Map<Long, Double> tsValues) {

        while (start < end) {
            if (tsValues.get(start) == null) {
                tsValues.put(start, 0D);
            }
            start += interval;
        }
    }

}
