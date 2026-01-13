package com.forum.boxchat.repository;

import com.forum.boxchat.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {

    Optional<List<Comment>> findByPost_Id(int postId);
    Optional<Comment> findById(int id);


}
