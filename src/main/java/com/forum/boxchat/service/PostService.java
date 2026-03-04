package com.forum.boxchat.service;



import com.forum.boxchat.dto.request.PostDtoRequest;
import com.forum.boxchat.dto.respone.PostDtoResponse;
import com.forum.boxchat.model.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PostService {

    //READ
    List<PostDtoResponse> findAllPost();

    PostDtoResponse findPostById(int id);

    List<PostDtoResponse> findPostByTitle(String title);

    //CREATE
    PostDtoResponse createPost(PostDtoRequest postDtoRequest);

    //Update
    PostDtoResponse updatePost(int id,PostDtoRequest postDtoRequest);

    //Delete
    void deletePost(int id);

    List<PostDtoResponse> findAllPostByCategory(String category);







}
