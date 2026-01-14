package com.forum.boxchat.repository;


import com.forum.boxchat.model.entity.BoxChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxChatRepository extends JpaRepository<BoxChat,Integer> {
}
