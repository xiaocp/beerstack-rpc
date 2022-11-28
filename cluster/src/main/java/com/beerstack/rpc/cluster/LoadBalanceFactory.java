package com.beerstack.rpc.cluster;

import com.beerstack.rpc.common.config.Property;
import com.beerstack.rpc.common.spi.ExtensionLoader;

/**
 * @author 肖长佩
 * @date 2022-10-14 13:51
 * @since 1.0.0
 */
public class LoadBalanceFactory {

    /**
     * 获取负载均衡策略
     *
     * @return
     */
    public static ILoadBalance getLoadBalance() {
        String loadBalance = Property.Cluster.LOADBALANCE;
        return ExtensionLoader.getExtensionLoader(ILoadBalance.class).getExtension(loadBalance);
    }

}
