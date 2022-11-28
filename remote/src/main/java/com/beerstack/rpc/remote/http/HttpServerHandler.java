package com.beerstack.rpc.remote.http;

import com.beerstack.rpc.remote.annotation.Container;
import com.beerstack.rpc.remote.dto.RpcRequestDto;
import com.beerstack.rpc.remote.dto.RpcResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author 肖长佩
 * @date 2022-10-14 17:07
 * @since 1.0.0
 */
public class HttpServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    public void handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // 从input流读取RpcRequestDto对象
            RpcRequestDto requestDto = (RpcRequestDto) objectInputStream.readObject();

            RpcResponseDto responseDto = Container.localInvoke(requestDto);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(response.getOutputStream());
            objectOutputStream.writeObject(responseDto);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
