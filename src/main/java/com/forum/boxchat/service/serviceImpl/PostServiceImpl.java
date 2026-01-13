package com.forum.boxchat.service.serviceImpl;


import com.forum.boxchat.dto.request.PostDtoRequest;
import com.forum.boxchat.dto.respone.PostDtoResponse;
import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import com.forum.boxchat.mapper.PostMapper;
import com.forum.boxchat.model.entity.Category;
import com.forum.boxchat.model.entity.Post;
import com.forum.boxchat.model.entity.User;
import com.forum.boxchat.repository.CategoryRepository;
import com.forum.boxchat.repository.PostRepository;
import com.forum.boxchat.repository.UserRepository;
import com.forum.boxchat.service.PostService;
import com.forum.boxchat.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    public final PostRepository postRepository;
    public final PostMapper postMapper;
    public final UserRepository userRepository;
    public final CategoryRepository categoryRepository;

    @Override
    public List<PostDtoResponse> findAllPost() {
        List<Post> posts = postRepository.findAll();

        if( posts.isEmpty() ) {
            throw new AppException(ErrorCode.POST_IS_EMPTY);
        }

        return posts.stream()
                .map(postMapper :: toResponse)
                .toList();
    }

    @Override
    public PostDtoResponse findPostById(int id) {

        Post post = postRepository.findPostById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        return postMapper.toResponse(post);
    }

    @Override
    public List<PostDtoResponse> findPostByTitle(String title) {
        List<Post> posts = postRepository.findPostByTitle(title)
                .orElseThrow(() -> new AppException(ErrorCode.POST_IS_EMPTY));

        return posts.stream()
                .map(postMapper :: toResponse)
                .toList();
    }

    @Override
    public PostDtoResponse createPost(PostDtoRequest postDtoRequest) {
        String idRaw = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID uuid = UUID.fromString(idRaw);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        log.info(user.toString());

        Category category = categoryRepository.findById(postDtoRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        log.info(category.toString());

        Post post = postMapper.toEntity(postDtoRequest);

        post.setUser(user);
        post.setCategory(category);
        SlugUtils slug = new SlugUtils();
        post.setSlug(slug.makeSlug(post.getTitle()));

        post = postRepository.save(post);
        return postMapper.toResponse(post);
    }

    @Override
    public PostDtoResponse updatePost(int id ,PostDtoRequest postDtoRequest) {

        Post post = postRepository.findPostById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        post.setTitle(postDtoRequest.getTitle());
        post.setContent(postDtoRequest.getContent());

        Category category = categoryRepository.findById(postDtoRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        post.setCategory(category);
        SlugUtils slug = new SlugUtils();
        post.setSlug(slug.makeSlug(post.getTitle()));
        postRepository.save(post);

        return postMapper.toResponse(post);
    }

    @Override
    public void deletePost(int id) {
        postRepository.deleteById(id);
    }








}
