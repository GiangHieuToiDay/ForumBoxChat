package com.forum.boxchat.repository;

import com.forum.boxchat.dto.respone.UserDtoRespone;
import com.forum.boxchat.model.entity.BoxParticipant;
import com.forum.boxchat.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface BoxParticipantRepository extends JpaRepository<BoxParticipant, Integer> {
    List<BoxParticipant> findByBoxChat_Id(int boxChatId);

    List<BoxParticipant> findByUser_Id(UUID userId);

    Optional<BoxParticipant> findByBoxChat_IdAndUser_Id(int boxChatId, UUID userId);

    boolean existsByBoxChat_IdAndUser_Id(int boxChatId, UUID userId);

    @Query("""
               select bp.user
               from BoxParticipant bp
               where bp.boxChat.id = :boxChatId
            """)
    List<User> getAllUserByBoxChatId(int  boxChatId);
}
