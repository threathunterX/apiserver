package com.threathunter.web.manager.rpc;

import com.threathunter.labrador.application.rpc.service.SplService;
import com.threathunter.labrador.rpc.client.RpcReference;
import com.threathunter.labrador.spl.check.CheckResponse;
import org.springframework.stereotype.Component;

/**
 * Created by wanbaowang on 17/10/30.
 */
@Component
public class SplClient {

    @RpcReference
    private SplService splService;

    public CheckResponse verify(String expression)  {
        return splService.check(expression);
    }


}
