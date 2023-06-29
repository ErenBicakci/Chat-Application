package com.erenbicakci.chatapplication.repository;

import com.erenbicakci.chatapplication.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
}
