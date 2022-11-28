package com.beerstack.rpc.registry.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:13
 * @since 1.0.0
 */
public class RedisClientServiceTest {

    @Test
    public void test(){
        RedisClientService.init("127.0.0.1:6379");
        Jedis jedis = RedisClientService.getJedis();
        jedis.lpush("bs-rpc", "com.beerstack.rpc.registry.redis.RedisClientService");
    }

}
