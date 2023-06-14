package com.xian.serviceorder.websocket;

import cn.hutool.json.JSONUtil;
import com.xian.entities.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@CrossOrigin
@ServerEndpoint("/serviceorder/bindingRecord/{orderNo}")
@Component
@Slf4j
public class WebSocket {
    private String orderNo;
    private Session session;
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    /**
     * 新建webSocket配置类
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    /**
     * 建立连接
     * @param
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("orderNo") String orderNo) {
        this.session = session;
        this.orderNo = orderNo;
        webSockets.add(this);
        System.out.println(orderNo);
        log.info("【新建连接】，连接总数:{}", webSockets.size());
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose(){
        webSockets.remove(this);
        log.info("【断开连接】，连接总数:{}", webSockets.size());
    }

    /**
     * 接收到信息
     * @param message
     */
    @OnMessage
    public void onMessage(String message){
        log.info("【收到】，订单号为"+orderNo+"客户端的信息:{}，当前连接总数:{}", message, webSockets.size());
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(R message){
        log.info("【广播发送】，信息:{}，当前连接总数:{}", message, webSockets.size());
        for (WebSocket webSocket : webSockets) {
            try {
                webSocket.session.getBasicRemote().sendText(JSONUtil.toJsonStr(message));
            } catch (IOException e) {
                log.info("【广播发送】，信息异常:{}", e.fillInStackTrace());
            }
        }
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessageToClient(String orderId,R message){
        log.info("【点对点发送】，目标：订单号为"+orderId+"的客户端信息:{}，当前连接总数:{}", message, webSockets.size());
        for (WebSocket webSocket : webSockets) {
            try {
                if (!StringUtils.hasText(orderId)) {
                    throw new RuntimeException("订单号为空！");
                } else if (webSocket.orderNo.equals(orderId)) {
                    webSocket.session.getBasicRemote().sendText(JSONUtil.toJsonStr(message));
                }
            } catch (IOException e) {
                log.info("【点对点发送】发送失败！信息异常:{}", e.fillInStackTrace());
            }
        }
    }

}
