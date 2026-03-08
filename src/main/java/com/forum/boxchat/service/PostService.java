package com.forum.boxchat.service;



import com.forum.boxchat.dto.request.PostDtoRequest;
import com.forum.boxchat.dto.respone.PostDtoResponse;
import com.forum.boxchat.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface PostService {

    //READ
//    List<PostDtoResponse> findAllPost();

    Page<PostDtoResponse> findAllPost(int page, int size);

    PostDtoResponse findPostById(int id);

    List<PostDtoResponse> findPostByTitle(String title);

    //CREATE
//    PostDtoResponse createPost(PostDtoRequest postDtoRequest);
    public PostDtoResponse createPost(PostDtoRequest postDtoRequest, MultipartFile image);

    //Update
    PostDtoResponse updatePost(int id,PostDtoRequest postDtoRequest);

    //Delete
    void deletePost(int id);

//    List<PostDtoResponse> findAllPostByCategory(String category);

    Page<PostDtoResponse> findAllPostByCategory(String category, int page, int size);







}
