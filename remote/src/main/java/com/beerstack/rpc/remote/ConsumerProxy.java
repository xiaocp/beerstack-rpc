package com.beerstack.rpc.remote;

import com.beerstack.rpc.registry.IRegister;
import com.beerstack.rpc.registry.RegisterFactory;
import com.beerstack.rpc.remote.dto.RpcRequestDto;
import com.beerstack.rpc.remote.dto.RpcResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author 肖长佩
 * @date 2022-10-14 17:26
 * @since 1.0.0
 */
public class ConsumerProxy {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerProxy.class);

    public static <T> Object create(final Class<T> interfaceClass) {
        return create(interfaceClass, "");
    }

    public static <T> Object create(final Class<T> interfaceClass, String serviceVersion) {
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}
                , new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        // 封装调用参数
                        RpcRequestDto requestDto = new RpcRequestDto();
                        requestDto.setRequestId(UUID.randomUUID().toString());
                        requestDto.setClassName(method.getDeclaringClass().getName());
                        requestDto.setMethodName(method.getName());
                        requestDto.setParameterTypes(method.getParameterTypes());
                        requestDto.setParameters(args);
                        requestDto.setServiceVersion(serviceVersion);

                        // 获取服务提供者
                        IRegister registrar = RegisterFactory.getRegister();
                        String service = interfaceClass.getName();
                        String serviceAddress = registrar.discover(service);
                        logger.debug("根据service名称查找地址, service={}, serviceAddress={}", service, serviceAddress);

                        // 调用上一章写的远程调用
                        RpcResponseDto rpcResponseDto = RemoteFactory.getConsumerService().execute(serviceAddress, requestDto);
                        logger.debug("rpcResponseDto={}", rpcResponseDto.toString());

                        return rpcResponseDto.getResult();
                    }
                });
    }

}
