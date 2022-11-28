package com.beerstack.rpc.sample.client;

import com.beerstack.rpc.remote.ConsumerProxy;
import com.beerstack.rpc.remote.IConsumerServer;
import com.beerstack.rpc.remote.RemoteFactory;
import com.beerstack.rpc.sample.api.HelloService;
import com.beerstack.rpc.sample.api.Person;

public class HelloClient2 {

    public static void main(String[] args) throws Exception {
        IConsumerServer consumerService = RemoteFactory.getConsumerService();
//        Object execute = consumerService.execute("127.0.0.1:20880", new RequestDTO());

        HelloService helloService = (HelloService) ConsumerProxy.create(HelloService.class, "v2");
        String result = helloService.hello(new Person("Beer", "Stack"));
        System.out.println(result);
        System.exit(0);
    }
}
