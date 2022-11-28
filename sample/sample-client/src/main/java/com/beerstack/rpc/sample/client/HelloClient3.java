package com.beerstack.rpc.sample.client;

import com.beerstack.rpc.remote.ConsumerProxy;
import com.beerstack.rpc.sample.api.HelloService;

public class HelloClient3 {

    public static void main(String[] args) throws Exception {

        int loopCount = 100;

        long start = System.currentTimeMillis();

        HelloService helloService = (HelloService) ConsumerProxy.create(HelloService.class);
        for (int i = 0; i < loopCount; i++) {
            String result = helloService.hello("World");
            System.out.println(result);
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("loop: " + loopCount);
        System.out.println("time: " + time + "ms");
        System.out.println("tps: " + (double) loopCount / ((double) time / 1000));

        System.exit(0);
    }
}
