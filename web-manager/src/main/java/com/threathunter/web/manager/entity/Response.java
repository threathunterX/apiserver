package com.threathunter.web.manager.entity;

import java.util.HashSet;
import java.util.Set;

public class Response
{
    private int status;
    private String msg;
    private long version;
    private Result result;
    
    public Result getResult() {
        return this.result;
    }
    
    public void addWhiteTrunk(final String trunk) {
        this.result.addWhiteTrunk(trunk);
    }
    
    public void addBlackTrunk(final String trunk) {
        this.result.addBlackTrunk(trunk);
    }
    
    public void adddTrunk(final String trunk) {
        this.result.adddTrunk(trunk);
    }
    
    public void setTrunks(final Set<String> trunks) {
        this.result.setTrunks(trunks);
    }
    
    public Response(final int status, final String msg, final long version) {
        this.status = status;
        this.msg = msg;
        this.version = version;
        this.result = new Result();
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(final int status) {
        this.status = status;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public long getVersion() {
        return this.version;
    }
    
    public void setVersion(final long version) {
        this.version = version;
    }
    
    public static Response getResponse(final long version) {
        return new Response(200, "ok", version);
    }
    
    class Result
    {
        private Set<String> whiteTrunks;
        private Set<String> blackTrunks;
        private Set<String> trunks;
        
        Result() {
            this.whiteTrunks = new HashSet<String>();
            this.blackTrunks = new HashSet<String>();
            this.trunks = new HashSet<String>();
        }
        
        public void addWhiteTrunk(final String trunk) {
            this.whiteTrunks.add(trunk);
        }
        
        public void addBlackTrunk(final String trunk) {
            this.blackTrunks.add(trunk);
        }
        
        public void adddTrunk(final String trunk) {
            this.trunks.add(trunk);
        }
        
        public Set<String> getWhiteTrunks() {
            return this.whiteTrunks;
        }
        
        public void setWhiteTrunks(final Set<String> whiteTrunks) {
            this.whiteTrunks = whiteTrunks;
        }
        
        public Set<String> getBlackTrunks() {
            return this.blackTrunks;
        }
        
        public void setBlackTrunks(final Set<String> blackTrunks) {
            this.blackTrunks = blackTrunks;
        }
        
        public Set<String> getTrunks() {
            return this.trunks;
        }
        
        public void setTrunks(final Set<String> trunks) {
            this.trunks = trunks;
        }
    }
}
