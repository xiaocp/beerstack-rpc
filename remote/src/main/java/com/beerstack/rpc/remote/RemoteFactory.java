package com.beerstack.rpc.remote;

import com.beerstack.rpc.common.config.Property;
import com.beerstack.rpc.common.spi.ExtensionLoader;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:23
 * @since 1.0.0
 */
public class RemoteFactory {

    public static IConsumerServer getConsumerService() {
        String protocol = Property.Rpc.PROTOCOL;
        return ExtensionLoader.getExtensionLoader(IConsumerServer.class).getExtension(protocol);
    }

    public static IProviderServer getProviderService() {
        String protocol = Property.Rpc.PROTOCOL;
        return ExtensionLoader.getExtensionLoader(IProviderServer.class).getExtension(protocol);
    }

}
