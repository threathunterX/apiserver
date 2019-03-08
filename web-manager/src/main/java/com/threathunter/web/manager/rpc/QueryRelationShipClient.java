package com.threathunter.web.manager.rpc;

import com.threathunter.labrador.application.rpc.service.QueryRelationShipService;
import com.threathunter.labrador.core.exception.LabradorException;
import com.threathunter.labrador.rpc.client.RpcReference;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QueryRelationShipClient {

    @RpcReference
    private QueryRelationShipService queryRelationShipService;

    public Map<String, Object> loadRelations(String key, String dimension) throws LabradorException {
        return queryRelationShipService.loadRelations(key, dimension);
    }

}
