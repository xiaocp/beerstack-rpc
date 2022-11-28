package com.beerstack.rpc.sample.server;


import com.beerstack.rpc.remote.annotation.Provider;
import com.beerstack.rpc.sample.api.HelloService;
import com.beerstack.rpc.sample.api.Person;

@Provider(HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }
}
