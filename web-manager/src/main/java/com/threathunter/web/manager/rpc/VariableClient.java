package com.threathunter.web.manager.rpc;

import com.threathunter.labrador.application.rpc.service.VariableQueryService;
import com.threathunter.labrador.core.exception.LabradorException;
import com.threathunter.labrador.rpc.client.RpcReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wanbaowang on 17/11/16.
 */
@Component
public class VariableClient {
    @RpcReference
    private VariableQueryService variableQueryService;

    public Map<String,Object> queryVariable(String pk, List<String> variables) throws LabradorException {
        return this.variableQueryService.query(pk, variables);
    }
}
