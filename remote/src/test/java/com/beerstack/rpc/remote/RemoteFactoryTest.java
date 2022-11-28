package com.beerstack.rpc.remote;

import com.beerstack.rpc.remote.dto.RpcRequestDto;
import com.beerstack.rpc.remote.dto.RpcResponseDto;
import com.beerstack.rpc.remote.http.HttpConsumerServer;
import com.beerstack.rpc.remote.http.HttpProviderServer;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 肖长佩
 * @date 2022-10-15 00:46
 * @since 1.0.0
 */
public class RemoteFactoryTest {

    @Test
    public void consumer() {
        IConsumerServer consumerService = RemoteFactory.getConsumerService();
        RpcResponseDto execute = consumerService.execute("127.0.0.1:20880", new RpcRequestDto());
    }

    @Test
    public void provider() throws IOException {
        IProviderServer providerServer = RemoteFactory.getProviderService();
        providerServer.start("127.0.0.1:20880");
        System.in.read();
    }


    @Test
    public void tomcatConsumer() {
        IConsumerServer consumerService = new HttpConsumerServer();
        RpcResponseDto execute = consumerService.execute("127.0.0.1:20880", new RpcRequestDto());
    }

    @Test
    public void tomcatProvider() throws IOException {
        IProviderServer providerServer = new HttpProviderServer();
        providerServer.start("127.0.0.1:20880");
        System.in.read();
    }

}
