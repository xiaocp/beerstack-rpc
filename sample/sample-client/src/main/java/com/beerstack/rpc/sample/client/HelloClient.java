package com.beerstack.rpc.sample.client;

import com.beerstack.rpc.remote.ConsumerProxy;
import com.beerstack.rpc.sample.api.HelloService;

public class HelloClient {

    public static void main(String[] args) throws Exception {
        HelloService helloService = (HelloService) ConsumerProxy.create(HelloService.class);
        String result = helloService.hello("xcp");
        System.out.println(result);
        System.exit(0);
    }
}
