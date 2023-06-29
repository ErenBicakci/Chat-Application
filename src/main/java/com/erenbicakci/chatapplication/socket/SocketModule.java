package com.erenbicakci.chatapplication.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.erenbicakci.chatapplication.entity.Message;
import com.erenbicakci.chatapplication.service.ChatService;
import com.erenbicakci.chatapplication.service.JwtService;
import com.erenbicakci.chatapplication.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SocketModule {

    private final SocketIOServer server;
    private final JwtService jwtService;
    private final ChatService chatService;

    private final MessageService messageService;

    public SocketModule(SocketIOServer server, JwtService jwtService, ChatService chatService, MessageService messageService) {
        this.server = server;
        this.jwtService = jwtService;
        this.chatService = chatService;
        this.messageService = messageService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("message", Message.class, onMessageReceived());
    }

    private DataListener<Message> onMessageReceived() {
        return ((senderClient, data, ackSender) -> {
            log.info("Message received: " + data.getContent() + " from: " + senderClient.getSessionId().toString());
            String room = senderClient.getHandshakeData().getSingleUrlParam("room");
            senderClient.getNamespace().getRoomOperations(room).getClients()
                    .forEach(x ->
                            {
                                if (!x.getSessionId().equals(senderClient.getSessionId())) {
                                    messageService.addMessageToChatRoom(room, data.getSender(), data.getReceiver(), data.getContent());
                                    x.sendEvent("message", data);
                                }
                            }
                    );
        });
    }


    private DisconnectListener onDisconnected() {
        return socketIOClient -> {
            String room = socketIOClient.getHandshakeData().getSingleUrlParam("room");
            socketIOClient.leaveRoom(room);
            socketIOClient.getNamespace().getRoomOperations(room).sendEvent("message", "Leave The Room : " + socketIOClient.getSessionId().toString(),room);
            log.info("Client disconnected: " + socketIOClient.getSessionId(),room);
        };
    }

    private ConnectListener onConnected() {
        return socketIOClient -> {
            String room = socketIOClient.getHandshakeData().getSingleUrlParam("room");
            //Room is available check
            if(chatService.chatRoomControl(room)){
                String token = socketIOClient.getHandshakeData().getSingleUrlParam("token");
                //Token is valid check
                if(jwtService.findUsernameFromTokenAndControl(token) != null){
                    //User is have access to room check
                    if(chatService.getRoomAccessControl(jwtService.findUsername(token),room)){
                        socketIOClient.joinRoom(room);
                        log.info("User is authorized : " + token );
                    }
                    else {
                        log.info("User is not authorized : " + token );
                        socketIOClient.disconnect();
                    }
                }
                else {
                    log.info("Token is not valid : " + token );
                    socketIOClient.disconnect();
                }
                socketIOClient.getHandshakeData();
                socketIOClient.getNamespace().getRoomOperations(room)
                        .sendEvent("message", "Join The Room : " + socketIOClient.getSessionId(),room);
                log.info("Client connected: " + socketIOClient.getSessionId().toString(),room);
            }
            else {
                log.info("Room is not valid : " + room );
                socketIOClient.disconnect();
            }



        };
    }
}
