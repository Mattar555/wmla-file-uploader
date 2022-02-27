package com.ibm.anz.wmla.sdk.ibmwmla.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import redis.clients.jedis.*;

import java.time.Duration;
import java.util.concurrent.Executor;

@Configuration
public class Config {

    @Value("${redis.poolConfig.maxTotal}")
    private int maxTotal;

    @Value("${redis.poolConfig.maxIdle}")
    private int maxIdle;

    @Value("${redis.poolConfig.minIdle}")
    private int minIdle;

    @Value("${redis.poolConfig.testOnBurrow}")
    private boolean testOnBurrow;

    @Value("${redis.poolConfig.testOnReturn}")
    private boolean testOnReturn;

    @Value("${redis.poolConfig.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${redis.poolConfig.blockWhenExhausted}")
    private boolean blockWhenExhausted;

    @Value("${redis.poolConfig.minEvictableIdleTime}")
    private int minEvictableIdleTime;

    @Value("${redis.poolConfig.timeBetweenEvictionRuns}")
    private int timeBetweenEvictionRuns;

    @Value("${redis.poolConfig.numTestsPerEviction}")
    private int numTestsPerEviction;

    @Value("${redis.info.host}")
    private String host;

    @Value("${redis.info.port}")
    private int port;

    @Value("${redis.info.timeout}")
    private int timeout;

    @Value("${redis.info.password}")
    private String password;

    @Bean(name="taskExecutor")
    public Executor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Uploader-");
        executor.initialize();
        return executor;
    }

    @Bean
    public JedisPool jedisPoolConfig() {
        // Create a pool of connections to use on demand. This ensures thread safety because multiple processes may be
        // writing and reading to Redis simultaneously. So as long as the resource to the pool when done.
        final JedisPoolConfig jedisPoolConfig = buildPoolConfig();
        return new JedisPool(
                jedisPoolConfig,
                host,
                port,
                timeout,
                password);
    }

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setTestOnBorrow(testOnBurrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestWhileIdle(testWhileIdle);
        poolConfig.setBlockWhenExhausted(blockWhenExhausted);
        poolConfig.setNumTestsPerEvictionRun(numTestsPerEviction);
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(minEvictableIdleTime));
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(timeBetweenEvictionRuns));
        return poolConfig;
    }
}
