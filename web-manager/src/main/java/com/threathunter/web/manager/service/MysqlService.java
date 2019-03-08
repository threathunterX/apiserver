package com.threathunter.web.manager.service;

import com.threathunter.web.manager.mysql.domain.*;
import com.threathunter.web.manager.mysql.mapper.*;
import com.threathunter.web.manager.mysql.util.MappingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wanbaowang on 17/9/19.
 */
@Service
public class MysqlService {

    @Autowired
    private TrunkMapper trunkMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private PorterMapper porterMapper;

    @Autowired
    private LogQueryMapper logQueryMapper;

    @Autowired
    private SlotNoticeMapper slotNoticeMapper;

    @Autowired
    private StrategyCustMapper strategyCustMapper;

    public void trunkSave(Trunk trunk) {
        trunkMapper.insertOrUpdate(trunk);
    }

    public List<Trunk> list() {
        return trunkMapper.list();
    }

    public void batchDelete(List<String> trunks) {
        trunkMapper.batchDelete(trunks);
    }

    public Notice getNotice() {
        return noticeMapper.noticeCheck();
    }

    public int insertPorter(Porter porter) {
        porter.setStatus(1);//when insert, set default to status 1;
        porterMapper.insert(porter);
        return porter.getId();
    }

    public Porter queryPorter(int id) {
        return porterMapper.getById(id);
    }

    public List<Porter> queryPorters() {
        return porterMapper.getAll();
    }

    public void deletePorter(int id) {
        porterMapper.updateStatus(id);
    }

    public LogQuery queryLogQuery(int id) {
        return logQueryMapper.getById(id);
    }

    public List<LogQuery> queryLogQuery() {
        return logQueryMapper.getAll();
    }

    public int insertLogQuery(LogQuery query) {
        logQueryMapper.insert(query);
        return query.getId();
    }

    public void updateLogQuery(LogQuery query) {
        logQueryMapper.update(query);
    }

    public void deleteLogQuery(LogQuery query) {
        logQueryMapper.delete(query.getId());
    }

    public void deleteLogQuery(int id) {
        logQueryMapper.delete(id);
    }

    public List<Integer> queryLogQueryIdsByNonSuccessStatus() {
        return logQueryMapper.getAllNonSuccessIds();
    }

    public List<Integer> queryLogQueryIdsBySuccessStatus() {
        return logQueryMapper.getAllSuccessIds();
    }

    public List<SlotNotice> querySlotNotice() {
        return slotNoticeMapper.getAll();
    }

    public Map<Long, Integer> selectStaticCount(Long startTime, Long endTime, Integer test) {
        return MappingHelper.<Long, Integer>toMap(slotNoticeMapper.getStaticCount(startTime, endTime, test));
    }

    public List<SlotNotice> filterSlotNoticeByExpireAndDecision(Long expire, String decision) {
        return slotNoticeMapper.filterByExpireAndDecision(expire, decision);
    }

    public List<StrategyCust> queryStrategyCust() {
        return strategyCustMapper.getAll();
    }

    public StrategyCust queryStrategyCust(int id) {
        return strategyCustMapper.getById(id);
    }

    public List<String> selectStrategyCustNameByTags(List<String> tags) {
        return strategyCustMapper.selectName(tags);
    }

    public List<SlotNotice> querySlotNoticeAtAggregate(SlotNoticeService.Criteria criteria, int offset, Integer rowCount) {
        return slotNoticeMapper.filterAggregate(criteria.isFilterExpire(), criteria.getDecisions(), criteria.getStrategies(),
                criteria.getCheckTypes(), criteria.getSceneTypes(), criteria.getFromTime(),
                criteria.getEndTime(), criteria.getKey(), criteria.getTest(), offset, rowCount);
    }

    public List<SlotNotice> querySlotNoticeAtNonAggregate(SlotNoticeService.Criteria criteria, int offset, Integer rowCount) {
        return slotNoticeMapper.filterNonAggregate(criteria.isFilterExpire(), criteria.getDecisions(), criteria.getStrategies(),
                criteria.getCheckTypes(), criteria.getSceneTypes(), criteria.getFromTime(),
                criteria.getEndTime(), criteria.getKey(), criteria.getTest(), offset, rowCount);
    }

    public int selectSlotNoticeCountAtAggregate(SlotNoticeService.Criteria criteria) {
        return slotNoticeMapper.selectCountAtAggregate(criteria.isFilterExpire(), criteria.getDecisions(), criteria.getStrategies(),
                criteria.getCheckTypes(), criteria.getSceneTypes(), criteria.getFromTime(),
                criteria.getEndTime(), criteria.getKey(), criteria.getTest());
    }

    public int selectSlotNoticeCountAtNonAggregate(SlotNoticeService.Criteria criteria) {
        return slotNoticeMapper.selectCountAtNonAggregate(criteria.isFilterExpire(), criteria.getDecisions(), criteria.getStrategies(),
                criteria.getCheckTypes(), criteria.getSceneTypes(), criteria.getFromTime(),
                criteria.getEndTime(), criteria.getKey(), criteria.getTest());
    }

    public List<Long> SelectSlotNoticeIdByExpireAndDecision() {
        return slotNoticeMapper.selectIdByExpireAndDecision();
    }

    public void addSlotNotice(final List<SlotNotice> notices) {
        this.slotNoticeMapper.insertList(notices);
    }

    public void addSlotNotice(final SlotNotice notice) {
        this.slotNoticeMapper.insert(notice);
    }

    public void deleteNotice(final String key, final Long fromtime, final Long endtime) {
        this.slotNoticeMapper.deleteByKeyAndTime(key, fromtime, endtime);
    }

}
