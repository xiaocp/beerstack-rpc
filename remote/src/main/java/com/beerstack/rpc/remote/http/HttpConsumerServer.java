package com.beerstack.rpc.remote.http;

import com.beerstack.rpc.remote.IConsumerServer;
import com.beerstack.rpc.remote.dto.RpcRequestDto;
import com.beerstack.rpc.remote.dto.RpcResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author 肖长佩
 * @date 2022-10-14 16:26
 * @since 1.0.0
 */
public class HttpConsumerServer implements IConsumerServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    @Override
    public RpcResponseDto execute(String address, RpcRequestDto requestDto) {
        String[] addrs = address.split(":");
        String ip = addrs[0];
        int port = Integer.parseInt(addrs[1]);

        try {
            URL url = new URL("http", ip, port, "/");
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);

            OutputStream outputStream = httpUrlConnection.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(requestDto);
            objectOutputStream.flush();
            objectOutputStream.close();


            InputStream inputStream = httpUrlConnection.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (RpcResponseDto) objectInputStream.readObject();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

}
