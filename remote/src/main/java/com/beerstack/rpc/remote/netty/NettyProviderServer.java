package com.beerstack.rpc.remote.netty;

import com.beerstack.rpc.remote.IProviderServer;
import com.beerstack.rpc.remote.annotation.Container;
import com.beerstack.rpc.remote.dto.RpcRequestDto;
import com.beerstack.rpc.remote.dto.RpcResponseDto;
import com.beerstack.rpc.remote.netty.codec.RpcDecoder;
import com.beerstack.rpc.remote.netty.codec.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:46
 * @since 1.0.0
 */
public class NettyProviderServer implements IProviderServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyProviderServer.class);

    @Override
    public void start(String selfAddress) {
        Container.registerSelf(selfAddress);

        String[] addrs = selfAddress.split(":");
        String ip = addrs[0];
        Integer port = Integer.parseInt(addrs[1]);

        publisher(ip, port);
    }

    private void publisher(String ip, Integer port) {
        // 启动服务
        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            //pipeline.addLast(new ObjectEncoder());
                            //pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(NettyProviderServer.class.getClassLoader())));
                            pipeline.addLast(new RpcDecoder(RpcRequestDto.class)); // 解码 RPC 请求
                            pipeline.addLast(new RpcEncoder(RpcResponseDto.class)); // 编码 RPC 响应
                            pipeline.addLast(new NettyProviderHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(ip, port).sync();
            logger.info("netty server is started... listen to {}:{}", ip, port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
