package com.erenbicakci.chatapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @Column(name="room_id")
    private String id;

    @OneToMany
    @JoinColumn(name="chat_room_id")
    private List<Message> messages;

    private String user1;
    private String user2;
}
