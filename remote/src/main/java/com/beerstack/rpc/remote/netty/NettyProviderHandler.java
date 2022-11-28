package com.beerstack.rpc.remote.netty;

import com.beerstack.rpc.remote.annotation.Container;
import com.beerstack.rpc.remote.dto.RpcRequestDto;
import com.beerstack.rpc.remote.dto.RpcResponseDto;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 肖长佩
 * @date 2022-10-14 17:32
 * @since 1.0.0
 */
public class NettyProviderHandler extends SimpleChannelInboundHandler<RpcRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(NettyProviderHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcRequestDto requestDto) {
        logger.info("receive request, body : {}", requestDto);

        RpcResponseDto responseDto = Container.localInvoke(requestDto);
        // 写入 RPC 响应对象并自动关闭连接
        ctx.writeAndFlush(responseDto);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("api caught exception", cause);
        ctx.close();
    }

}
