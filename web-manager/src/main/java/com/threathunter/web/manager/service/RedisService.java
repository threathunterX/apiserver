package com.threathunter.web.manager.service;

import com.threathunter.web.manager.utils.ControllerException;
import com.threathunter.web.manager.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public long getCurrentVersion() {
        Object obj = this.redisTemplate.opsForValue().get(Constant.TRUNK_VERSIONS);
        return null == obj ? 0L : Long.valueOf(obj.toString());
    }

    public long incrementVersion() {
        long currentVersion = (Long) this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.incr(Constant.TRUNK_VERSIONS.getBytes());
            }
        });
        return currentVersion;
    }

    public void report(Set<String> urls, Set<String> trunks, long version) {

        long currentVersion = getCurrentVersion();
        if (currentVersion != version) {
            throw new ControllerException("version not matched, current version is " + currentVersion + ", but request version is " + version);
        }
        this.redisTemplate.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.multi();
                for (String url : urls) {
                    redisOperations.opsForSet().add(Constant.URL_KEYS, url);
                }
                for (String trunk : trunks) {
                    redisOperations.opsForSet().add(Constant.TRUNK_KEYS, trunk);
                }
                redisOperations.exec();
                return null;
            }
        });
    }

    public Set<String> getTrunks() {
        Set<String> redisTrunks = this.redisTemplate.opsForSet().members(Constant.TRUNK_KEYS);
        return redisTrunks;
    }

    public void deleteTrunks() {
        this.redisTemplate.delete(Constant.TRUNK_KEYS);
    }


}
