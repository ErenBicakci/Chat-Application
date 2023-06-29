package com.erenbicakci.chatapplication.service;

import com.erenbicakci.chatapplication.dto.ChatRoomDto;
import com.erenbicakci.chatapplication.entity.ChatRoom;
import com.erenbicakci.chatapplication.entity.User;
import com.erenbicakci.chatapplication.exception.ChatRoomNotFoundException;
import com.erenbicakci.chatapplication.exception.UserNotFoundException;
import com.erenbicakci.chatapplication.log.CustomLogDebug;
import com.erenbicakci.chatapplication.repository.ChatRoomRepository;
import com.erenbicakci.chatapplication.repository.UserRepository;
import com.erenbicakci.chatapplication.util.DtoConvert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ChatService {

    @Value("${secret-key}")
    private String SECRET_KEY;

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final DtoConvert dtoConvert;

    public ChatService(UserRepository userRepository, ChatRoomRepository chatRoomRepository, DtoConvert dtoConvert) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.dtoConvert = dtoConvert;
    }


    @CustomLogDebug
    public ChatRoomDto getChatRoom(String sender, String receiver) {

        User senderUser = userRepository.findByUsername(sender).orElseThrow(() -> new UserNotFoundException("User not found"));
        User receiverUser = userRepository.findByUsername(receiver).orElseThrow(() -> new UserNotFoundException("User not found"));
        User[] users = {senderUser, receiverUser};
        users = sortUsers(users);
        String plainText = users[0].getUsername() + "-" + users[1].getUsername() + "-" + SECRET_KEY;
        String encryptedText = MD5Crypt(plainText);

        ChatRoom chatRoom = chatRoomRepository.findById(encryptedText).orElse(null);
        if (chatRoom == null) {
            chatRoom = new ChatRoom();
            chatRoom.setId(encryptedText);
            chatRoom.setUser1(users[0].getUsername());
            chatRoom.setUser2(users[1].getUsername());
            chatRoomRepository.save(chatRoom);
            return dtoConvert.convertChatRoomToChatRoomDto(chatRoom);
            }
        return dtoConvert.convertChatRoomToChatRoomDto(chatRoom);
    }



    @CustomLogDebug
    public boolean getRoomAccessControl(String username, String roomId){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));
        return chatRoom.getUser1().equals(user.getUsername()) || chatRoom.getUser2().equals(user.getUsername());
    }

    @CustomLogDebug
    public boolean chatRoomControl(String id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElse(null);

        return chatRoom != null;
    }


    public User[] sortUsers(User[] users) {
        for (int i = 0;i < users.length;i++){
            for (int j = i+1;j < users.length;j++){
                if (users[i].getId() > users[j].getId()){
                    User temp = users[i];
                    users[i] = users[j];
                    users[j] = temp;
                }
            }
        }
        return users;
    }


    private String MD5Crypt(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] data = plainText.getBytes();
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
