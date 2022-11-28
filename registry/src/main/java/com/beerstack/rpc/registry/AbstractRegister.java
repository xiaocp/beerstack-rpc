package com.beerstack.rpc.registry;

import com.beerstack.rpc.cluster.ILoadBalance;
import com.beerstack.rpc.cluster.LoadBalanceFactory;
import com.beerstack.rpc.common.config.Property;

import java.util.List;

/**
 * @author 肖长佩
 * @date 2022-10-14 13:38
 * @since 1.0.0
 */
public abstract class AbstractRegister implements IRegister {

    protected static final String FOLDER = "/bs-registers";
    protected static final String SEPARATOR = "/";

    public AbstractRegister() {
        String address = Property.Registry.ADDRESS;
        init(address);
    }

    @Override
    public String discover(String serviceName) {
        List<String> providers = lookup(serviceName);
        ILoadBalance loadBalance = LoadBalanceFactory.getLoadBalance();
        return loadBalance.select(providers);
    }

    /**
     * 初始化
     *
     * @param address
     */
    protected abstract void init(String address);

    /**
     * 查找
     *
     * @param serviceName
     * @return
     */
    protected abstract List<String> lookup(String serviceName);

}
