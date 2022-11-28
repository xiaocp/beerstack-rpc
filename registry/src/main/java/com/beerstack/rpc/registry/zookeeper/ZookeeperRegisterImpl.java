package com.beerstack.rpc.registry.zookeeper;

import com.beerstack.rpc.registry.AbstractRegister;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 肖长佩
 * @date 2022-10-14 14:38
 * @since 1.0.0
 */
public class ZookeeperRegisterImpl extends AbstractRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegisterImpl.class);

    private static final int ZK_SESSION_TIMEOUT = 5000;
    private static final int ZK_CONNECTION_TIMEOUT = 1000;
    private ZkClient zkClient;
    private Map<String, List<String>> serviceProviderMap = new HashMap<>();

    @Override
    protected void init(String address) {
        zkClient = new ZkClient(address, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("connect zookeeper");
    }

    @Override
    protected List<String> lookup(String serviceName) {
        // 获取 service 节点
        String servicePath = FOLDER + SEPARATOR + serviceName + SEPARATOR + "providers";
        LOGGER.debug("lookup registry service path : {}", servicePath);
        if (!zkClient.exists(servicePath)) {
            throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
        }
        List<String> addressList = zkClient.getChildren(servicePath);
        if (CollectionUtils.isEmpty(addressList)) {
            throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
        }
        serviceProviderMap.put(servicePath, addressList);
        zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
            // 第一个参数path是节点的路径，第二个参数childs是子节点的信息
            @Override
            public void handleChildChange(String path, List<String> childList) throws Exception {
                serviceProviderMap.put(path, childList);
            }
        });
        return addressList;
    }

    @Override
    public void register(String serviceAddress, String serviceName) {
        // 创建 registry 节点（持久）
        String registryPath = FOLDER;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            LOGGER.debug("create registry node : {}", registryPath);
        }
        // 创建 server 节点（持久）
        String servicePath = registryPath + SEPARATOR + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            LOGGER.debug("create service node : {}", servicePath);
        }
        // 创建 address 节点（临时）
        String providersPath = servicePath + SEPARATOR + "providers";
        if (!zkClient.exists(providersPath)) {
            zkClient.createPersistent(providersPath);
            LOGGER.debug("create service node : {}", providersPath);
        }
        String addressPath = providersPath + SEPARATOR + serviceAddress;
        zkClient.createEphemeral(addressPath);
        LOGGER.debug("create address node: {}", addressPath);
    }

}
