package com.forum.boxchat.service;

import com.forum.boxchat.dto.request.CommentDtoRequest;
import com.forum.boxchat.dto.respone.CommentDtoResponse;
import com.forum.boxchat.model.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CommentService {

    List<CommentDtoResponse> findByPost_Id(int postId);

    CommentDtoResponse createComment(CommentDtoRequest commentDtoRequest);

    CommentDtoResponse updateComment(int id,CommentDtoRequest commentDtoRequest);

    void deleteComment(int id);





}
