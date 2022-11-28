package com.beerstack.rpc.cluster.loadbalance;

import com.beerstack.rpc.cluster.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @author 肖长佩
 * @date 2022-10-14 13:57
 * @since 1.0.0
 */
public class RandomLoadBalanceImpl extends AbstractLoadBalance {

    @Override
    public String doSelect(List<String> providers) {
        int len = providers.size();
        Random random = new Random();
        int number = random.nextInt(len);
        return providers.get(number);
    }

}
