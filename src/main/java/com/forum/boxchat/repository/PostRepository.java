package com.forum.boxchat.repository;

import com.forum.boxchat.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findPostById(int id);
    Optional<List<Post>> findPostByTitle(String title);
}
