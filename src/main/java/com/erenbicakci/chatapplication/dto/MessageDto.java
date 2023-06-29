package com.erenbicakci.chatapplication.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    @Id
    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private String date;
    private String chatRoomId;


}
