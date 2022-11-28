package com.beerstack.rpc.cluster.loadbalance;

import com.beerstack.rpc.cluster.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 肖长佩
 * @date 2022-10-14 13:57
 * @since 1.0.0
 */
public class RoundLoadBalanceImpl extends AbstractLoadBalance {

    private AtomicInteger previous = new AtomicInteger(0);

    @Override
    public String doSelect(List<String> providers) {
        int size = providers.size();
        if (previous.get() >= size) {
            previous.set(0);
        }
        String provider = providers.get(previous.get());
        previous.set(previous.get() + 1);
        return provider;
    }

}
