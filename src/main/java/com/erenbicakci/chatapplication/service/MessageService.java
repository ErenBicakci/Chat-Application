package com.erenbicakci.chatapplication.service;

import com.erenbicakci.chatapplication.entity.Message;
import com.erenbicakci.chatapplication.log.CustomLogDebug;
import com.erenbicakci.chatapplication.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final ChatService chatService;
    private final MessageRepository messageRepository;

    public MessageService(ChatService chatService, MessageRepository messageRepository) {
        this.chatService = chatService;
        this.messageRepository = messageRepository;
    }

    @CustomLogDebug
    public void addMessageToChatRoom(String chatRoomId, String sender, String receiver, String content) {
        Message message = Message.builder()
                .chatRoomId(chatRoomId)
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .date(new java.util.Date().toString())
                .build();
        messageRepository.save(message);
    }
}
