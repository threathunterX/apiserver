package com.threathunter.web.manager.rpc;

import com.threathunter.labrador.application.rpc.service.GlobalVariableQueryService;
import com.threathunter.labrador.core.exception.LabradorException;
import com.threathunter.labrador.rpc.client.RpcReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GlobalVariableClient {

    @RpcReference
    private GlobalVariableQueryService globalVariableQueryService;

    public Map<String, Object> query(String day, List<String> variables) throws LabradorException {
        return globalVariableQueryService.query(day, variables);
    }

}