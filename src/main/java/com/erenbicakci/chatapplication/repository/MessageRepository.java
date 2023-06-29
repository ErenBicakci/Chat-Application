package com.erenbicakci.chatapplication.repository;

import com.erenbicakci.chatapplication.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
