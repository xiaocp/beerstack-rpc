package com.beerstack.rpc.sample.server;

import com.beerstack.rpc.remote.annotation.Provider;
import com.beerstack.rpc.sample.api.HelloService;
import com.beerstack.rpc.sample.api.Person;

@Provider(value = HelloService.class, version = "v2")
public class HelloServiceImpl2 implements HelloService {

    @Override
    public String hello(String name) {
        return "你好! " + name;
    }

    @Override
    public String hello(Person person) {
        return "你好! " + person.getFirstName() + " " + person.getLastName();
    }
}
