package com.beerstack.rpc.sample.client;

import com.beerstack.rpc.remote.ConsumerProxy;
import com.beerstack.rpc.sample.api.HelloService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloClient4 {

    public static void main(String[] args) throws Exception {

        int threadNum = 10;
        int loopCount = 500;

        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        final CountDownLatch latch = new CountDownLatch(loopCount);

        try {
            long start = System.currentTimeMillis();

            for (int i = 0; i < loopCount; i++) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        HelloService helloService = (HelloService) ConsumerProxy.create(HelloService.class);
                        String result = helloService.hello("World");
                        System.out.println(result);
                        latch.countDown();
                    }
                });
            }
            latch.await();

            long time = System.currentTimeMillis() - start;
            System.out.println("thread: " + threadNum);
            System.out.println("loop: " + loopCount);
            System.out.println("time: " + time + "ms");
            System.out.println("tps: " + (double) loopCount / ((double) time / 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        System.exit(0);
    }
}
