package com.ibm.anz.wmla.sdk.ibmwmla.task;

import com.ibm.anz.wmla.sdk.ibmwmla.enumerations.Schema;
import com.ibm.anz.wmla.sdk.ibmwmla.enumerations.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.util.Map;
import java.util.Set;


@Component
public class Poller {

    private static final Logger LOG = LoggerFactory.getLogger(Poller.class);
    private static final String MATCH_ALL = "*";

    @Value("${redis.search.prefix}")
    private String prefix;

    @Value("${volume.path}")
    private String volumePath;

    @Autowired
    private JedisPool jedisPool;

    @Scheduled(fixedDelay = 5000) // Every 5 seconds.
    public void pollStatus() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> identifiers = jedis.keys(prefix.concat(MATCH_ALL));
            for (String identifier : identifiers) {
                Map<String, String> values = jedis.hgetAll(identifier);
                if (values.get(Schema.STATUS.getValue()).equals(States.PENDING.getValue())) {
                    if (checkSize(values)) {
                        jedis.hset(identifier, Schema.STATUS.getValue(), States.SUCCESS.getValue());
                    }
                }
            }
        }
    }

    private boolean checkSize(Map<String, String> values) {
        String fileNameInQuestion = values.get(Schema.FILENAME.getValue());
        long expectedSize = Long.parseLong(values.get(Schema.FILESIZE.getValue()));
        LOG.debug("Name: {}, Size: {}", fileNameInQuestion, expectedSize);
        long currentSize = new File(volumePath.concat(fileNameInQuestion)).length();
        return currentSize == expectedSize;
    }
}
