package com.forum.boxchat.repository;

import com.forum.boxchat.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    Page<Message> findByBoxChatId(Integer boxChatId, Pageable pageable);


}
