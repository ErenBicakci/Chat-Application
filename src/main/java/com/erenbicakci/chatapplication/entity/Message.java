package com.erenbicakci.chatapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="message_id")
    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private String date;
    @Column(name="chat_room_id")
    private String chatRoomId;

}
