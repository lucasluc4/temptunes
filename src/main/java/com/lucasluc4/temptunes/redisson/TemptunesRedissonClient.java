package com.lucasluc4.temptunes.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TemptunesRedissonClient {

    private RedissonClient redissonClient;

    private Environment environment;

    @Autowired
    public TemptunesRedissonClient(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        Config config = new Config();
        config.useSingleServer().setAddress(environment.getProperty("db.redis.host"));

        redissonClient = Redisson.create(config);
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
