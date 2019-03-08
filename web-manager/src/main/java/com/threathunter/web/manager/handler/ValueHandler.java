package com.threathunter.web.manager.handler;

import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public abstract class ValueHandler {
    protected ValueHandler next;

    public void add(ValueHandler handler) {
        if (next == null) {
            next = handler;
        } else {
            next.add(handler);
        }
    }

    public void handle(String var, Object value, Map<String, Object> result) {
        Object ret = execute(var, value);
        if (ret == null) {
            if (next != null) {
                next.handle(var, value, result);
            }
        } else {
            result.put(var, ret);
        }
    }

    public abstract Object execute(String var, Object value);
}
