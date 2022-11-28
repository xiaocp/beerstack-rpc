package com.beerstack.rpc.remote.netty;

import com.beerstack.rpc.remote.dto.RpcResponseDto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 肖长佩
 * @date 2022-10-14 17:38
 * @since 1.0.0
 */
public class NettyConsumerHandler extends SimpleChannelInboundHandler<RpcResponseDto> {

    private static final Logger logger = LoggerFactory.getLogger(NettyConsumerHandler.class);

    private RpcResponseDto responseDto;

    public RpcResponseDto getResponseDto() {
        return responseDto;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponseDto responseDto) throws Exception {
        this.responseDto = responseDto;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("api caught exception", cause);
        ctx.close();
    }

}

