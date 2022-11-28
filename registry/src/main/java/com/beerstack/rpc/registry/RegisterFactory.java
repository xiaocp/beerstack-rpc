package com.beerstack.rpc.registry;

import com.beerstack.rpc.common.config.Property;
import com.beerstack.rpc.common.spi.ExtensionLoader;

/**
 * @author 肖长佩
 * @date 2022-10-14 11:44
 * @since 1.0.0
 */
public class RegisterFactory {

    /**
     * 获取实例
     *
     * @return
     */
    public static IRegister getRegister() {
        String protocol = Property.Registry.PROTOCOL;
        return ExtensionLoader.getExtensionLoader(IRegister.class).getExtension(protocol);
    }

}
