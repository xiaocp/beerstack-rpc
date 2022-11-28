package com.beerstack.rpc.common.config;

import com.beerstack.rpc.common.util.PropertyUtil;

/**
 * @author 肖长佩
 * @date 2022-10-14 15:06
 * @since 1.0.0
 */
public class Property {

    public final static String REGISTRY_PROTOCOL_KEY = "rpc.registry.protocol";
    public final static String REGISTRY_ADDRESS_KEY = "rpc.registry.address";
    public final static String CLUSTER_LOADBALANCE_KEY = "rpc.cluster.loadbalance";
    public final static String REMOTE_PROTOCOL_KEY = "rpc.remote.protocol";
    public final static String REMOTE_PROVIDER_PACKAGES_KEY = "rpc.remote.provider.packages";

    public final static String REGISTRY_PROTOCOL_DEFAULT_VAL = "";
    public final static String REGISTRY_ADDRESS_DEFAULT_VAL = "";
    public final static String CLUSTER_LOADBALANCE_DEFAULT_VAL = "round";
    public final static String REMOTE_PROTOCOL_DEFAULT_VAL = "netty";
    public final static String REMOTE_PROVIDER_PACKAGES_DEFAULT_VAL = "com.beerstack";

    public static class Registry {
        public final static String PROTOCOL = PropertyUtil.getInstance().getIfNullThrowEx(REGISTRY_PROTOCOL_KEY);
        public final static String ADDRESS = PropertyUtil.getInstance().getIfNullThrowEx(REGISTRY_ADDRESS_KEY);
    }

    public static class Cluster {
        public final static String LOADBALANCE =
                PropertyUtil.getInstance().getOrDefault(CLUSTER_LOADBALANCE_KEY, CLUSTER_LOADBALANCE_DEFAULT_VAL);
    }

    public static class Rpc {
        public final static String PROTOCOL =
                PropertyUtil.getInstance().getOrDefault(REMOTE_PROTOCOL_KEY, REMOTE_PROTOCOL_DEFAULT_VAL);
        public final static String PACKAGES =
                PropertyUtil.getInstance().getIfNullThrowEx(REMOTE_PROVIDER_PACKAGES_KEY);
    }

}
