package com.beerstack.rpc.registry.redis;

import com.beerstack.rpc.registry.AbstractRegister;

import java.util.List;

/**
 * @author 肖长佩
 * @date 2022-10-14 14:38
 * @since 1.0.0
 */
public class RedisRegisterImpl extends AbstractRegister {

    @Override
    protected void init(String address) {
        RedisClientService.init(address);
    }

    @Override
    protected List<String> lookup(String serviceName) {
        return RedisClientService.getJedis().lrange(FOLDER + SEPARATOR + serviceName, 0, -1);
    }

    @Override
    public void register(String serviceAddress, String serviceName) {
        RedisClientService.getJedis().lpush(FOLDER + SEPARATOR + serviceName, serviceAddress);
    }

}
