package com.forum.boxchat.repository;


import com.forum.boxchat.model.entity.BoxChat;
import com.forum.boxchat.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoxChatRepository extends JpaRepository<BoxChat,Integer> {

}
