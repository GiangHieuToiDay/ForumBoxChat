package com.forum.boxchat.repository;


import com.forum.boxchat.model.entity.BoxChat;
import com.forum.boxchat.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoxChatRepository extends JpaRepository<BoxChat,Integer> {

    @Query("SELECT b FROM BoxChat b " +
            "JOIN b.participants p1 " +
            "JOIN b.participants p2 " +
            "WHERE b.type = 'PRIVATE' " +
            "AND p1.user.id = :user1Id " +
            "AND p2.user.id = :user2Id")
    Optional<BoxChat> findPrivateBox(@Param("user1Id") UUID user1Id, @Param("user2Id") UUID user2Id);


    List<BoxChat> findByCreatedBy(User createdBy);

}
