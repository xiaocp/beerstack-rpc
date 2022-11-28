package com.beerstack.rpc.registry.redis;

import com.beerstack.rpc.registry.IRegister;
import org.junit.Test;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:13
 * @since 1.0.0
 */
public class RedisRegisterImplTest {

    @Test
    public void test(){
        IRegister registrar = new RedisRegisterImpl();

        registrar.register("127.0.0.1:62880", "com.beerstack.rpc.registry.redis.RedisClientService");
        registrar.discover("com.beerstack.rpc.registry.redis.RedisClientService");
    }

}
