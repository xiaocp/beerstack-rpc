package com.beerstack.rpc.remote;

import com.beerstack.rpc.common.annotation.BeerSPI;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:19
 * @since 1.0.0
 */
@BeerSPI("netty")
public interface IProviderServer {

    /**
     * 服务提供者启动
     *
     * @param selfAddress
     */
    void start(String selfAddress);

}
