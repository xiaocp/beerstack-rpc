package com.beerstack.rpc.registry;

import com.beerstack.rpc.common.annotation.BeerSPI;

/**
 * @author 肖长佩
 * @date 2022-10-14 11:31
 * @since 1.0.0
 */
@BeerSPI("zookeeper")
public interface IRegister {

    /**
     * 注册服务
     *
     * @param serviceAddress 服务地址
     * @param serviceName    服务名称
     */
    void register(String serviceAddress, String serviceName);

    /**
     * 根据服务名称查找服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    String discover(String serviceName);

}
