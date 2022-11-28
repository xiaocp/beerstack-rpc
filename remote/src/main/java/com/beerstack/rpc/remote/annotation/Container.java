package com.beerstack.rpc.remote.annotation;

import com.beerstack.rpc.common.config.Property;
import com.beerstack.rpc.registry.IRegister;
import com.beerstack.rpc.registry.RegisterFactory;
import com.beerstack.rpc.remote.dto.RpcRequestDto;
import com.beerstack.rpc.remote.dto.RpcResponseDto;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.beerstack.rpc.common.config.Property.REMOTE_PROVIDER_PACKAGES_KEY;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:47
 * @since 1.0.0
 */
public class Container {

    private static final Logger logger = LoggerFactory.getLogger(Container.class);
    private static final IRegister REGISTER = RegisterFactory.getRegister();
    private static final Map<String, Object> PROVIDERS = new HashMap<String, Object>();
    public static volatile boolean init = false;

    static {
        init();
    }

    public static void init() {
        String packages = Property.Rpc.PACKAGES;
        logger.debug("load provider packages : {}", packages);
        if (StringUtils.isBlank(packages)) {
            throw new RuntimeException("load provider packages is empty. property key is [" + REMOTE_PROVIDER_PACKAGES_KEY + "]");
        }
        List<URL> urlList = Arrays.stream(StringUtils.split(packages))
                .map(ClasspathHelper::forPackage).flatMap(Collection::stream)
                .collect(Collectors.toList());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(urlList)
                .setScanners(new TypeAnnotationsScanner()));
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Provider.class, true);
        for (Class<?> clazz : classes) {
            try {
                Provider annotation = clazz.getAnnotation(Provider.class);
                Object provider = clazz.newInstance();
                String serviceName = getServiceVersionName(annotation);
                logger.debug("注册服务serviceName={}, provider={}", serviceName, provider);
                if (!PROVIDERS.containsKey(serviceName)) {
                    PROVIDERS.put(serviceName, provider);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取provider注解的serviceName
     *
     * @param annotation
     * @return
     */
    public static String getServiceVersionName(Provider annotation) {
        String serviceName = annotation.value().getCanonicalName();
        String serviceVersion = annotation.version();
        return getServiceVersionName(serviceName, serviceVersion);
    }

    /**
     * 获取serviceName全名
     *
     * @param serviceName
     * @param serviceVersion
     * @return
     */
    public static String getServiceVersionName(String serviceName, String serviceVersion) {
        if (StringUtils.isNotEmpty(serviceVersion)) {
            serviceName += "-" + serviceVersion;
        }
        return serviceName;
    }

    public static void registerSelf(String selfAddress) {
        for (String service : PROVIDERS.keySet()) {
            REGISTER.register(selfAddress, service);
        }
    }

    public static Map<String, Object> getProviders() {
        return PROVIDERS;
    }

    /**
     * @param requestDto
     * @return
     */
    public static RpcResponseDto localInvoke(RpcRequestDto requestDto) {
        Object result = new Object();
        try {
            // 检查provider是否存在该类的服务
            String serviceName = getServiceVersionName(requestDto.getClassName(), requestDto.getServiceVersion());
            if (Container.getProviders().containsKey(serviceName)) {
                Object provider = Container.getProviders().get(serviceName);
                // 反射调用
                Class<?> providerClazz = provider.getClass();
                Method method = providerClazz.getMethod(requestDto.getMethodName(), requestDto.getParameterTypes());
                result = method.invoke(provider, requestDto.getParameters());
            } else {
                logger.warn("serviceName : {} , is not exist.", serviceName);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }

        // 封装响应dto
        RpcResponseDto rpcResponseDto = new RpcResponseDto();
        rpcResponseDto.setResult(result);
        rpcResponseDto.setRequestId(requestDto.getRequestId());

        return rpcResponseDto;
    }

}
