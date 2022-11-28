package com.beerstack.rpc.remote;

import com.beerstack.rpc.common.annotation.BeerSPI;
import com.beerstack.rpc.remote.dto.RpcRequestDto;
import com.beerstack.rpc.remote.dto.RpcResponseDto;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:19
 * @since 1.0.0
 */
@BeerSPI("netty")
public interface IConsumerServer {

    /**
     * 消费者执行调用
     *
     * @param address
     * @param requestDto
     * @return
     */
    RpcResponseDto execute(String address, RpcRequestDto requestDto);

}
