package com.beerstack.rpc.registry.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 肖长佩
 * @date 2022-10-14 15:41
 * @since 1.0.0
 */
public class RedisClientService {

    private final static Logger logger = LoggerFactory.getLogger(RedisClientService.class);
    private static int MAX_ACTIVE = 1024;
    private static int MAX_IDLE = 200;
    private static int MAX_WAIT = 10000;
    private static int TIMEOUT = 10000;
    private static boolean TEST_ON_BORROW = true;
    private static JedisPool jedisPool = null;

    public static void init(String address) {
        if (jedisPool == null) {
            synchronized (RedisClientService.class) {
                if (jedisPool == null) {
                    try {
                        String[] split = address.split(":");
                        String host = split[0];
                        Integer port = Integer.parseInt(split[1]);
                        JedisPoolConfig config = new JedisPoolConfig();
                        config.setMaxTotal(MAX_ACTIVE);
                        config.setMaxIdle(MAX_IDLE);
                        config.setMaxWaitMillis(MAX_WAIT);
                        config.setTestOnBorrow(TEST_ON_BORROW);
                        jedisPool = new JedisPool(config, host, port, TIMEOUT);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            } else {
                throw new RuntimeException("call RedisClientService.getJedis, connection is closed");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /***
     * 释放资源
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

}
