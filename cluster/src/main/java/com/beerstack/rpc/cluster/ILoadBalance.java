package com.beerstack.rpc.cluster;

import com.beerstack.rpc.common.annotation.BeerSPI;

import java.util.List;

/**
 * @author 肖长佩
 * @date 2022-10-14 13:47
 * @since 1.0.0
 */
@BeerSPI("random")
public interface ILoadBalance {

    /**
     * 负载均衡选择一个实例
     *
     * @param providers 实例列表
     * @return 筛选的实例
     */
    String select(List<String> providers);

}
