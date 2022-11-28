package com.beerstack.rpc.remote.http;

import com.beerstack.rpc.remote.IProviderServer;
import com.beerstack.rpc.remote.annotation.Container;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:26
 * @since 1.0.0
 */
public class HttpProviderServer implements IProviderServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpProviderServer.class);

    @Override
    public void start(String selfAddress) {
        Container.registerSelf(selfAddress);
        // 获取端口和ip
        String[] addrs = selfAddress.split(":");
        String ip = addrs[0];
        int port = Integer.parseInt(addrs[1]);

        Tomcat tomcat = new Tomcat();
        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");
        Connector connector = new Connector();
        // 设置端口
        connector.setPort(port);

        // 设置ip
        Engine engine = new StandardEngine();
        engine.setDefaultHost(ip);

        Host host = new StandardHost();
        host.setName(ip);

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);

        // 绑定自定义DispatcherServlet
        tomcat.addServlet(contextPath, "dispatcher", new DispatcherServlet());
        context.addServletMappingDecoded("/*", "dispatcher");

        try {
            tomcat.start();
            logger.info("http server is started... listen to {}:{}", ip, port);
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

}
