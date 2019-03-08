package com.threathunter.web.manager.service;

import lombok.Data;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.threathunter.web.manager.mysql.domain.*;
import java.util.stream.*;
import java.util.*;

@Service
public class SlotNoticeService
{
    @Autowired
    MysqlService mysqlService;
    
    public Map<String, Object> aggregateNotice(final Criteria criteria) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        boolean ifAggregate = false;
        if (criteria.getEndTime() != null && criteria.getFromTime() != null && criteria.getEndTime() - criteria.getFromTime() <= 3600000L) {
            ifAggregate = true;
        }
        int count = 0;
        int offset = 0;
        final Integer limit = criteria.getLimit();
        if (criteria.getPage() > 1) {
            offset = (criteria.getPage() - 1) * limit;
        }
        final List<SlotNotice> notices = new ArrayList<SlotNotice>();
        if (ifAggregate) {
            count = mysqlService.selectSlotNoticeCountAtAggregate(criteria);
            List<SlotNotice> slotNotices = mysqlService.querySlotNoticeAtAggregate(criteria, offset, limit);
            slotNotices.stream().parallel().forEach(n -> {
                n.setAggregated(true);
            });
            notices.addAll(slotNotices);
        }
        else {
            count = this.mysqlService.selectSlotNoticeCountAtNonAggregate(criteria);
            final List<SlotNotice> slotNotices = this.mysqlService.querySlotNoticeAtNonAggregate(criteria, offset, limit);
            slotNotices.stream().parallel().forEach(n -> {
                n.setAggregated(false);
                n.setCount(1);
                return;
            });
            notices.addAll(slotNotices);
        }
        final int totalPage = (int)Math.ceil(count * 1.0 / limit);
        retMap.put("count", count);
        retMap.put("total", totalPage);
        retMap.put("notices", notices);
        return retMap;
    }

    /**
     * rd['key_in_whitelist'] = True
     * rd['whitelist'] = whitelists.get((rd.key, rd.check_type, rd.test))
     * whitelist >>> check_type=_.check_type,key=_.key,
     * expire=_.expire, test=_.test, remark=_.remark,
     * decision=_.decision)
     *
     * @param notices
     */
    public void appendWhiteInfo(List<SlotNotice> notices) {
        List<Long> whiteIds = mysqlService.SelectSlotNoticeIdByExpireAndDecision();
        notices.parallelStream().forEach(n -> {
            Long id = n.getId();
            for (Long wId : whiteIds) {
                if (n.getId().compareTo(wId) == 0) {
                    n.setKey_in_whitelist(true);
                    Map<String, Object> map = new HashMap<>();
                    map.put("check_type", n.getCheck_type());
                    map.put("key", n.getKey());
                    map.put("expire", n.getExpire());
                    map.put("test", n.getTest());
                    map.put("remark", n.getRemark());
                    map.put("decision", n.getDecision());
                    n.setWhitelist(map);
                }
            }
        });
    }

    public Map<List<String>, Object> getWhiteListWhole(Long expire) {
        if (expire == null)
            expire = System.currentTimeMillis();
        List<SlotNotice> slotNotices = mysqlService.filterSlotNoticeByExpireAndDecision(expire, "accept");
        Map<List<String>, Object> retMap = new HashMap<>();
        slotNotices.stream().forEach(notice -> {
            String key = notice.getKey();
            String check_type = notice.getCheck_type();
            Long test = notice.getTest();
            String ifTest = test.compareTo(0L) == 0 ? "FALSE" : "TRUE";
            /***
             * from python:
             * ((_.key, _.check_type, _.test), dict(check_type=_.check_type,key=_.key,
             expire=_.expire, test=_.test, remark=_.remark,
             decision=_.decision))
             */
            Map<String, Object> map = new HashMap();
            map.put("check_type", notice.getCheck_type());
            map.put("key", notice.getKey());
            map.put("expire", notice.getExpire());
            map.put("test", notice.getTest());
            map.put("remark", notice.getRemark());
            map.put("decision", notice.getDecision());
            retMap.put(Collections.unmodifiableList(Arrays.asList(key, check_type, ifTest)),
                    map
            );
        });
        return retMap;
    }
    
    public void add(final List<SlotNotice> notices) {
        this.mysqlService.addSlotNotice(notices);
    }
    
    public void add(final SlotNotice notice) {
        this.mysqlService.addSlotNotice(notice);
    }
    
    public void delete(final String key, final Long fromtime, final Long endtime) {
        this.mysqlService.deleteNotice(key, fromtime, endtime);
    }

    @Data
    public static class Criteria
    {
        private String key;
        private Long endTime;
        private List<String> strategies;
        private List<String> sceneTypes;
        private List<String> checkTypes;
        private List<String> decisions;
        private boolean filterExpire;
        private Integer page;
        private Integer limit;
        private Long fromTime;
        private Long test;
    }
}
