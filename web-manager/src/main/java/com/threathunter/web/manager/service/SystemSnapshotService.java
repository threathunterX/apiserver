package com.threathunter.web.manager.service;

import com.threathunter.model.Event;
import com.threathunter.web.manager.utils.TypeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Slf4j
@Service
public class SystemSnapshotService {
    static String version = "";

    @Autowired
    BabelService babelService;

    @Autowired
    MetricsAgentService agentService;

    private static final Logger log = LoggerFactory.getLogger(SystemSnapshotService.class);

    private static String getVersion() {
        File file = new File("/home/threathunter/nebula/VERSION");
        if (file.exists()) {
            try {
                FileReader reader = new FileReader(file);
                BufferedReader br = new BufferedReader(reader);
                String ret = br.readLine();
                return ret;
            } catch (FileNotFoundException e) {
                log.error("file", e);
            } catch (IOException e) {
                log.error("IO", e);
            }
        }
        return "";
    }

    public Map<String, Object> getLicenseInfo() {
        Event returnEvent = babelService.getLicenseInfo();
        Map<String, Object> propertyValues = returnEvent.getPropertyValues();
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("expire", propertyValues.get("days"));
        if ("".equals(version))
            version = getVersion();
        retMap.put("version", version);
        return retMap;
    }

    public Map<String, Object> getDigest() {
        Double space = agentService.getSpace();
        List<Double> cpu = agentService.getCpu();
        List<Double> memory = agentService.getMemory();
        Map<String, Object> ret = new HashMap<>();
        ret.put("space", TypeFormatter.convertDoubleToPercentageInteger(space));
        ret.put("cpu", TypeFormatter.convertDoubleToPercentageInteger(cpu));
        ret.put("memory", TypeFormatter.convertDoubleToPercentageInteger(memory));
        return ret;
    }


}
