package com.beerstack.rpc.remote.netty;

import com.beerstack.rpc.remote.IConsumerServer;
import com.beerstack.rpc.remote.dto.RpcRequestDto;
import com.beerstack.rpc.remote.dto.RpcResponseDto;
import com.beerstack.rpc.remote.netty.codec.RpcDecoder;
import com.beerstack.rpc.remote.netty.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:45
 * @since 1.0.0
 */
public class NettyConsumerServer implements IConsumerServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyConsumerServer.class);

    @Override
    public RpcResponseDto execute(String address, RpcRequestDto requestDto) {
        String[] addrs = address.split(":");
        String host = addrs[0];
        Integer port = Integer.parseInt(addrs[1]);

        final NettyConsumerHandler consumerHandler = new NettyConsumerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            //pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(ConsumerProxy.class.getClassLoader())));
                            //pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new RpcEncoder(RpcRequestDto.class)); // 编码 RPC 请求
                            pipeline.addLast(new RpcDecoder(RpcResponseDto.class)); // 解码 RPC 响应
                            pipeline.addLast(consumerHandler);
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();

            Channel channel = future.channel();
            channel.writeAndFlush(requestDto);
            logger.info("send request, body : {}", requestDto);
            channel.closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            group.shutdownGracefully();
        }
        return consumerHandler.getResponseDto();
    }

}
