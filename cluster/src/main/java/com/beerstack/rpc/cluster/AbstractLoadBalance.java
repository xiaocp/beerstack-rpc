package com.beerstack.rpc.cluster;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author 肖长佩
 * @date 2022-10-14 13:55
 * @since 1.0.0
 */
public abstract class AbstractLoadBalance implements ILoadBalance {

    @Override
    public String select(List<String> providers) {
        if (CollectionUtils.isEmpty(providers)) {
            return null;
        }
        if (providers.size() == 1) {
            return providers.get(0);
        }
        return doSelect(providers);
    }

    public abstract String doSelect(List<String> providers);

}
