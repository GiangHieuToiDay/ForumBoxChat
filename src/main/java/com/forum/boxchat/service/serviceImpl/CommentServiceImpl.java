package com.forum.boxchat.service.serviceImpl;

import com.forum.boxchat.dto.request.CommentDtoRequest;
import com.forum.boxchat.dto.respone.CommentDtoResponse;
import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import com.forum.boxchat.mapper.CommentMapper;
import com.forum.boxchat.model.entity.Comment;
import com.forum.boxchat.model.entity.Post;
import com.forum.boxchat.model.entity.User;
import com.forum.boxchat.repository.CommentRepository;
import com.forum.boxchat.repository.PostRepository;
import com.forum.boxchat.repository.UserRepository;
import com.forum.boxchat.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    public final CommentRepository commentRepository;
    public final CommentMapper commentMapper;
    public final UserRepository userRepository;
    public final PostRepository postRepository;


    @Override
    public List<CommentDtoResponse> findByPost_Id(int postId) {

        Post post = postRepository.findPostById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        List<Comment> comments = commentRepository.findByPost_Id(postId).orElse(new ArrayList<>());

        return comments.stream()
                .map(commentMapper :: toResponseDto)
                .toList();
    }

    @Override
    public CommentDtoResponse createComment(CommentDtoRequest commentDtoRequest) {

        String idRaw = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID uuid = UUID.fromString(idRaw);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findPostById(commentDtoRequest.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentMapper.toEntity(commentDtoRequest);
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);

        return commentMapper.toResponseDto(comment);
    }

    @Override
    public CommentDtoResponse updateComment(int id,CommentDtoRequest commentDtoRequest) {
        Comment coment = commentRepository.findById(id)
                .orElseThrow(() ->  new AppException(ErrorCode.COMMENT_NOT_FOUND));

        coment.setContent(commentDtoRequest.getContent());

        String idRaw = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID uuid = UUID.fromString(idRaw);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if( user.getId() != coment.getUser().getId()){
            throw new AppException(ErrorCode.COMMENT_NOT_BY_USER);
        }

        commentRepository.save(coment);

        return commentMapper.toResponseDto(coment);
    }

    @Override
    public void deleteComment(int id) {
        Comment coment = commentRepository.findById(id)
                .orElseThrow(() ->  new AppException(ErrorCode.COMMENT_NOT_FOUND));

        String idRaw = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID uuid = UUID.fromString(idRaw);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if( user.getId() != coment.getUser().getId()){
            throw new AppException(ErrorCode.COMMENT_NOT_BY_USER);
        }

        commentRepository.deleteById(id);

    }
}
