package com.erenbicakci.chatapplication.controller;

import com.erenbicakci.chatapplication.dto.ChatRoomDto;
import com.erenbicakci.chatapplication.log.CustomLogDebug;
import com.erenbicakci.chatapplication.log.CustomLogInfo;
import com.erenbicakci.chatapplication.service.ChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @CustomLogInfo
    @GetMapping("/chatroom")
    public ChatRoomDto getChat(@RequestParam String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return chatService.getChatRoom(auth.getName(), username);
    }
}
