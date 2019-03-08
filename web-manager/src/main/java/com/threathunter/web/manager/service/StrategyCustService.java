package com.threathunter.web.manager.service;

import com.threathunter.web.common.utils.CollectionUtils;
import com.threathunter.web.manager.mysql.domain.StrategyCust;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Slf4j
@Service
public class StrategyCustService implements InitializingBean {
    @Autowired
    MysqlService mysqlService;

    Gson gson;
    Map<String, Object> cache = new ConcurrentHashMap<>();

    Map<String, Object> getStrategyWeigh() {
        List<StrategyCust> strategies = mysqlService.queryStrategyCust();
        Map<String, Object> retMap = new HashMap<>();
        for (StrategyCust strategy : strategies) {
            String config = strategy.getConfig();
            if (config != null) ;
            Map<String, Object> configMap = gson.fromJson(config, Map.class);
            if (configMap.get("terms") != null) {
                List<Map<String, Object>> terms = (List<Map<String, Object>>) configMap.get("terms");
                if (terms != null) {
                    for (Map<String, Object> term : terms) {
                        Map<String, Object> left = (Map<String, Object>) term.get("left");
                        String subtype = (String) left.get("subtype");
                        if ("setblacklist".equals(subtype)) {
                            Map<String, Object> blackListInfo = (Map<String, Object>) left.get("config");
                            if (blackListInfo != null) {
                                Map<String, Object> m = new HashMap<>();
                                /**
                                 *
                                 * from python
                                 *
                                 *
                                 * 'app': s.app,
                                 * 'name': s.name,
                                 * 'tags': (s.tags or '').split(','),
                                 * ' category': s.category,
                                 * 'score': s.score,
                                 * 'expire': s.endeffect,
                                 * 'remark': s.remark,
                                 * 'test': True if s.status == 'test' else False,
                                 * 'scope': term.get('scope', ''),
                                 * 'checkpoints': blacklist_info.get('checkpoints', ''),
                                 * 'checkvalue': blacklist_info.get('checkvalue', ''),
                                 * 'checktype': blacklist_info.get('checktype', ''),
                                 * 'decision': blacklist_info.get('decision', ''),
                                 * 'ttl': blacklist_info.get('ttl', 300)
                                 */
                                m.put("app", strategy.getApp());
                                m.put("name", strategy.getName());
                                m.put("tags", (strategy.getTags()) == null ? new ArrayList<String>() : strategy.getTags().split(","));
                                m.put("category", strategy.getCategory());
                                m.put("score", strategy.getScore());
                                m.put("expire", strategy.getEndeffect());
                                m.put("remark", strategy.getRemark());
                                m.put("test", "test".equals(strategy.getStatus()));
                                m.put("scope", term.get("scope") == null ? "" : term.get("scope"));
                                Object checkPoints = blackListInfo.get("checkpoints");
                                Object checkValue = blackListInfo.get("checkvalue");
                                Object checkType = blackListInfo.get("checktype");
                                Object decision = blackListInfo.get("decision");
                                Object ttl = blackListInfo.get("ttl");
                                m.put("checkpoints", checkPoints == null ? "" : checkPoints);
                                m.put("checkvalue", checkValue == null ? "" : checkValue);
                                m.put("checktype", checkType == null ? "" : checkType);
                                m.put("decision", decision == null ? "" : decision);
                                m.put("ttl", ttl == null ? "" : ttl);
                                retMap.put(strategy.getName(), m);
                            } else {
                                log.error("app:{}, name:{} 的策略没有设置黑名单的配置", strategy.getApp(), strategy.getName());
                            }

                        }

                    }
                }

            }
        }
        return retMap;
    }


    public List<Object> filterStrategyWeigh(List<String> tags) {
        if (cache.size() == 0) {
            Map<String, Object> strategyWeighMap = getStrategyWeigh();
            cache.putAll(strategyWeighMap);
        }
        List<Object> filtedStrategyWeigh = cache.values().stream().parallel().filter(v -> {
            Map<String, Object> vm = (Map<String, Object>) v;
            List<String> vtags = (List<String>) vm.get("tags");
            List<String> intersection = CollectionUtils.intersection(tags, vtags);
            if (intersection.size() > 0)
                return true;
            return false;
        }).collect(Collectors.toList());
        if (filtedStrategyWeigh == null || filtedStrategyWeigh.size() == 0)
            return Collections.EMPTY_LIST;
        else
            return filtedStrategyWeigh;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        gson = new GsonBuilder().create();
    }


    public List<String> selectNameByTags(List<String> tags) {
        List<String> names = mysqlService.selectStrategyCustNameByTags(tags);
        return names;
    }
}
