package com.ibm.anz.wmla.sdk.ibmwmla.service;


import com.ibm.anz.wmla.sdk.ibmwmla.enumerations.Schema;
import com.ibm.anz.wmla.sdk.ibmwmla.enumerations.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.UUID;

@Service
public class RedisService {

    private static final Logger LOG = LoggerFactory.getLogger(RedisService.class);

    @Value("${redis.search.prefix}")
    private String prefix;

    @Autowired
    private JedisPool jedisPool;

    public String populateEntry(UUID uuid, String fileName, long fileSize) {
        // Try with resources statement means we do not need to worry about manually closing the Jedis Resource.
        // This is done implicitly, and as such, the resource is guaranteed to be returned to the pool
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(prefix.concat(uuid.toString()), Schema.FILENAME.getValue(), fileName);
            jedis.hset(prefix.concat(uuid.toString()), Schema.FILESIZE.getValue(), String.valueOf(fileSize));
            jedis.hset(prefix.concat(uuid.toString()), Schema.STATUS.getValue(), States.PENDING.getValue());
        }
        return prefix.concat(uuid.toString());
    }

    public void setValue(String testVal) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set("TEST", testVal);
        }
    }

    public String fetchStatus(String identifier) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> values = jedis.hgetAll(identifier);
            return values.get(Schema.STATUS.getValue());
        }
    }
}
