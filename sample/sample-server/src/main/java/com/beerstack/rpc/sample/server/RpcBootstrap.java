package com.beerstack.rpc.sample.server;

import com.beerstack.rpc.remote.IProviderServer;
import com.beerstack.rpc.remote.RemoteFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcBootstrap.class);

    public static void main(String[] args) {
        LOGGER.debug("start server");
        IProviderServer server = RemoteFactory.getProviderService();
        server.start("127.0.0.1:20880");
    }
}
