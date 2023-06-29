package com.erenbicakci.chatapplication.util;

import com.erenbicakci.chatapplication.dto.ChatRoomDto;
import com.erenbicakci.chatapplication.dto.MessageDto;
import com.erenbicakci.chatapplication.entity.ChatRoom;
import com.erenbicakci.chatapplication.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class DtoConvert {

    public ChatRoomDto convertChatRoomToChatRoomDto(ChatRoom chatRoom) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setId(chatRoom.getId());
        chatRoomDto.setUser1(chatRoom.getUser1());
        chatRoomDto.setUser2(chatRoom.getUser2());
        try {
            chatRoomDto.setMessages(chatRoom.getMessages().stream().map(this::convertMessageToMessageRoomDto).toList());

        }
        catch (Exception e) {
            chatRoomDto.setMessages(null);
        }
        return chatRoomDto;
    }


    public MessageDto convertMessageToMessageRoomDto(Message message){

        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setSender(message.getSender());
        messageDto.setReceiver(message.getReceiver());
        messageDto.setContent(message.getContent());
        messageDto.setChatRoomId(message.getChatRoomId());
        messageDto.setDate(message.getDate());
        return messageDto;
    }
}
