package com.threathunter.web.manager.mysql.domain;

public enum LogQueryStatus
{
    WAIT("wait"), 
    PROCESS("process"), 
    SUCCESS("success"), 
    FAILED("failed");
    
    private final String status;
    
    private LogQueryStatus(final String status) {
        this.status = status;
    }
    
    public String getValue() {
        return this.status;
    }
    
    public static LogQueryStatus toType(final String status) {
        for (final LogQueryStatus logQueryStatus : values()) {
            if (logQueryStatus.getValue().equalsIgnoreCase(status)) {
                return logQueryStatus;
            }
        }
        return null;
    }
}
