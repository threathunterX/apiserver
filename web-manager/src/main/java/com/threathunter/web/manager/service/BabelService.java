package com.threathunter.web.manager.service;

import org.springframework.beans.factory.*;
import com.threathunter.babel.meta.*;
import com.threathunter.web.manager.mysql.domain.*;
import java.util.*;
import org.slf4j.*;
import com.google.gson.*;
import com.threathunter.babel.rpc.impl.*;
import java.util.concurrent.*;
import com.threathunter.babel.rpc.*;
import com.threathunter.model.*;
import org.springframework.stereotype.Service;

@Service
public class BabelService implements InitializingBean
{
    private static final Logger log;
    Map<String, BabelUnit> serviceManager;
    TopicService queryNotifyBabelService;
    
    private void register(final String key, final String metaFile) {
        try {
            final ServiceMeta metaFromResourceFile = ServiceMetaUtil.getMetaFromResourceFile(metaFile);
            this.serviceManager.put(key, new BabelUnit(metaFromResourceFile));
        }
        catch (Exception e) {
            BabelService.log.error(String.format("key:%s, meta:%s register fail", key, metaFile));
        }
    }
    
    public Event getLicenseInfo() {
        final Event event = new Event("nebula", "licenseinfo", "");
        event.setTimestamp(System.currentTimeMillis());
        final Map<String, Object> properties = (Map<String, Object>)Collections.EMPTY_MAP;
        final BabelUnit unit = this.serviceManager.get("licenseinfo");
        final Event ret = unit.rpc(event);
        return ret;
    }
    
    public Event createLogQuery(final LogQuery query) {
        final BabelUnit persistentquery = this.serviceManager.get("persistentquery_crud");
        final Event request = new Event();
        request.setApp("__all__");
        request.setName("logquery");
        request.setKey("");
        request.setValue(0.0);
        request.setTimestamp(System.currentTimeMillis());
        final Map<String, Object> properties = new HashMap<String, Object>();
        final List<String> showCols = query.getShowColumns();
        properties.put("show_cols", showCols);
        properties.put("fromtime", query.getFromTime());
        properties.put("endtime", query.getEndTime());
        properties.put("name", query.getEventName());
        properties.put("terms", query.getTerms());
        properties.put("action_type", "create");
        properties.put("id", query.getId());
        request.setPropertyValues((Map)properties);
        final Event response = persistentquery.rpc(request);
        return response;
    }
    
    public Event deleteLogQuery(final int id) {
        final BabelUnit persistentquery = this.serviceManager.get("persistentquery_crud");
        final Event request = new Event();
        request.setApp("__all__");
        request.setName("logquery");
        request.setKey("");
        request.setValue(0.0);
        request.setTimestamp(System.currentTimeMillis());
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("action_type", "delete");
        properties.put("id", id);
        request.setPropertyValues((Map)properties);
        final Event response = persistentquery.rpc(request);
        return response;
    }
    
    public Event fetchLogQuery(final int id, final int page, final int pageCount) {
        final BabelUnit persistentquery = this.serviceManager.get("persistentquery_crud");
        final Event request = new Event();
        request.setApp("__all__");
        request.setName("logquery");
        request.setKey("");
        request.setValue(0.0);
        request.setTimestamp(System.currentTimeMillis());
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("action_type", "fetch");
        properties.put("id", id);
        properties.put("page", String.valueOf(page));
        properties.put("page_count", String.valueOf(pageCount));
        request.setPropertyValues((Map)properties);
        final Event response = persistentquery.rpc(request);
        return response;
    }
    
    public Event progressLogQuery() {
        final Event lastEvent = this.queryNotifyBabelService.getLastEvent();
        return lastEvent;
    }
    
    public Event offlineKeyStatQuery(final Map<String, Object> map) {
        final BabelUnit keyStatQuery = this.serviceManager.get("offline_slot_variablequery");
        final Event request = new Event();
        request.setApp("__all__");
        request.setName("offline_slot_variablequery");
        request.setKey("");
        request.setTimestamp(System.currentTimeMillis());
        final Map<String, Object> properties = new HashMap<String, Object>();
        List<String> keys = (List<String>)map.get("keys");
        String dimension = (String)map.get("dimension");
        if (keys == null || keys.size() == 0) {
            keys = new ArrayList<String>();
            keys.add("__GLOBAL__");
            dimension = "global";
        }
        properties.put("keys", keys);
        properties.put("dimension", dimension);
        properties.put("timestamp", map.get("timestamp"));
        properties.put("var_list", map.get("variables"));
        request.setPropertyValues((Map)properties);
        BabelService.log.info(">>>offlineVariableQuery.request={}", (Object)request);
        final Event response = keyStatQuery.rpc(request);
        BabelService.log.info(">>>offlineVariableQuery.response={}", (Object)response);
        return response;
    }
    
    public Event onlineKeyStatQuery(final Map<String, Object> map) {
        final BabelUnit keyStatQuery = this.serviceManager.get("online_slot_variablequery");
        final Event request = new Event();
        request.setApp("__all__");
        request.setName("online_slot_variablequery");
        request.setKey("");
        request.setTimestamp(System.currentTimeMillis());
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("keys", map.get("keys"));
        properties.put("key_type", map.get("dimension"));
        properties.put("timestamp", map.get("timestamp"));
        properties.put("var_list", map.get("variables"));
        if (map.get("subkeys") != null) {
            properties.put("subkeys", map.get("subkeys"));
        }
        request.setPropertyValues((Map)properties);
        BabelService.log.info(">>>onlineVariableQuery.request={}", (Object)request);
        final Event response = keyStatQuery.rpc(request);
        BabelService.log.info(">>>onlineVariableQuery.response={}", (Object)response);
        return response;
    }
    
    public void afterPropertiesSet() throws Exception {
        this.serviceManager = new HashMap<String, BabelUnit>();
        this.register("licenseinfo", "LicenseInfo_redis.service");
        this.register("persistentquery_crud", "PersistentQuery_redis.service");
        this.register("offline_slot_variablequery", "OfflineSlotVariableQuery_redis.service");
        this.register("clickstreamquery", "ClickstreamQuery_redis.service");
        this.register("online_slot_variablequery", "OnlineSlotVariableQuery_redis.service");
        final ServiceMeta meta = ServiceMetaUtil.getMetaFromResourceFile("PersistentQueryNotify_redis.service");
        this.queryNotifyBabelService = new TopicService(meta);
        final ServiceContainer container = (ServiceContainer)new ServerContainerImpl();
        container.addService((com.threathunter.babel.rpc.Service)this.queryNotifyBabelService);
        container.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> container.stop()));
    }
    
    public Event clickStreamQueryPeriod(final Map<String, Object> param) {
        final BabelUnit clickstreamquery = this.serviceManager.get("clickstreamquery");
        final Event request = new Event();
        request.setApp("__all__");
        request.setName("clickstreamquery");
        request.setKey((String)param.get("key"));
        request.setValue(20.0);
        request.setTimestamp(System.currentTimeMillis());
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.putAll(param);
        properties.put("query_type", "clicks_period");
        request.setPropertyValues((Map)properties);
        final Event ret = clickstreamquery.rpc(request);
        return ret;
    }
    
    public Event clickStreamQueryVisit(final Map<String, Object> param) {
        final BabelUnit clickstreamquery = this.serviceManager.get("clickstreamquery");
        final Event request = new Event();
        request.setApp("__all__");
        request.setName("clickstreamquery");
        request.setKey((String)param.get("key"));
        request.setValue(20.0);
        request.setTimestamp(System.currentTimeMillis());
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.putAll(param);
        properties.put("query_type", "visit_stream");
        request.setPropertyValues((Map)properties);
        final Event ret = clickstreamquery.rpc(request);
        return ret;
    }
    
    public Event getClickQueryDetail(final String key, final String dimension, final Long fromTime, final Long endTime, final String query_type, List query, int clickscount) {
        BabelService.log.info(">>>>>>getClickQueryDetail:param(key:{},dimension:{},fromTime:{},endTime:{},query_type:{},query:{},clickscount:{})", new Object[] { key, dimension, fromTime, endTime, query_type, query, clickscount });
        final BabelUnit clickstreamquery = this.serviceManager.get("clickstreamquery");
        final Event request = new Event();
        request.setApp("__all__");
        request.setName("clickstreamquery");
        request.setKey(key);
        request.setTimestamp(System.currentTimeMillis());
        if (clickscount == 0) {
            clickscount = 20;
        }
        if (query == null) {
            query = new ArrayList();
        }
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("clickscount", clickscount);
        properties.put("query", query);
        properties.put("query_type", query_type);
        properties.put("from_time", fromTime);
        properties.put("end_time", endTime);
        properties.put("dimension", dimension);
        request.setPropertyValues((Map)properties);
        final Event ret = clickstreamquery.rpc(request);
        BabelService.log.info("<<<<<<getClickQueryDetail:result:{}", (Object)ret);
        return ret;
    }
    
    public Event getClickQueryDetail(final String key, final String dimension, final Long fromTime, final Long endTime, final String query_type) {
        return this.getClickQueryDetail(key, dimension, fromTime, endTime, query_type, null, 0);
    }
    
    public Event getClickQueryDetail(final String key, final String dimension, final Long fromTime, final Long endTime, final String query_type, final List query) {
        return this.getClickQueryDetail(key, dimension, fromTime, endTime, query_type, query, 0);
    }
    
    static {
        log = LoggerFactory.getLogger((Class)BabelService.class);
    }
    
    private static class BabelUnit
    {
        private ServiceMeta serviceMeta;
        private ServiceClient serviceClient;
        private ThreadLocal<ServiceClient> serviceClientHolder;
        Gson gson;
        
        public BabelUnit(final ServiceMeta meta) {
            this.gson = new GsonBuilder().create();
            this.serviceMeta = meta;

            this.serviceClientHolder = ThreadLocal.withInitial(() -> {
                serviceClient = new ServiceClientImpl(meta);
                serviceClient.start();
                Runtime.getRuntime().addShutdownHook(new Thread(() -> serviceClient.stop()));
                return serviceClient;
            });
        }
        
        public ServiceMeta getServiceMeta() {
            return this.serviceMeta;
        }
        
        public void setServiceMeta(final ServiceMeta serviceMeta) {
            this.serviceMeta = serviceMeta;
        }
        
        public ServiceClient getServiceClinet() {
            return this.serviceClientHolder.get();
        }
        
        public void setServiceClinet(final ServiceClient serviceClinet) {
        }
        
        public Event rpc(final Event event) {
            Event ret = null;
            final Long start = System.currentTimeMillis();
            try {
                BabelService.log.debug(">>>eventID:{}, babel:rpc start:{}", (Object)event.getId(), (Object)start);
                ret = this.serviceClientHolder.get().rpc(event, this.getServiceMeta().getName(), 5L, TimeUnit.SECONDS);
                final Long end = System.currentTimeMillis();
                BabelService.log.debug(">>>eventID:{},babel:rpc end,{}", (Object)event.getId(), (Object)end);
                BabelService.log.debug("<<<eventID:{},babel:rpc, duration:{}", (Object)event.getId(), (Object)(end - start));
            }
            catch (RemoteException e) {
                final Long end2 = System.currentTimeMillis();
                final Long duration = end2 - start;
                BabelService.log.error(String.format("===eventID:" + event.getId() + ",meta:" + this.gson.toJson((Object)this.getServiceMeta()) + ",duration:" + duration, new Object[0]), (Throwable)e);
                return null;
            }
            return ret;
        }
    }
    
    public class TopicService implements com.threathunter.babel.rpc.Service
    {
        ServiceMeta meta;
        Event lastEvent;
        
        public TopicService(final ServiceMeta meta) {
            this.meta = meta;
        }
        
        public Event process(final Event event) {
            return this.lastEvent = event;
        }
        
        public Event getLastEvent() {
            return this.lastEvent;
        }
        
        public ServiceMeta getServiceMeta() {
            return this.meta;
        }
        
        public EventMeta getRequestEventMeta() {
            return null;
        }
        
        public EventMeta getResponseEventMeta() {
            return null;
        }
        
        public void close() {
        }
    }
}
