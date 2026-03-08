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
import com.forum.boxchat.service.CloudinaryService;
import com.forum.boxchat.service.PostService;
import com.forum.boxchat.service.UserService;
import com.forum.boxchat.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    public final PostRepository postRepository;
    public final PostMapper postMapper;
    public final UserRepository userRepository;
    public final CategoryRepository categoryRepository;
    public final CloudinaryService cloudinaryService;

//    @Override
//    public List<PostDtoResponse> findAllPost() {
//        List<Post> posts = postRepository.findAll();
//
//        if( posts.isEmpty() ) {
//            throw new AppException(ErrorCode.POST_IS_EMPTY);
//        }
//
//        return posts.stream()
//                .map(postMapper :: toResponse)
//                .toList();
//    }

    @Override
    public Page<PostDtoResponse> findAllPost(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Post> postPage = postRepository.findAll(pageable);

        if (postPage.isEmpty()) {
            throw new AppException(ErrorCode.POST_IS_EMPTY);
        }

        return postPage.map(postMapper::toResponse);
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
    public PostDtoResponse createPost(PostDtoRequest postDtoRequest, MultipartFile image) {

        String idRaw = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID uuid = UUID.fromString(idRaw);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(postDtoRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Post post = postMapper.toEntity(postDtoRequest);

        post.setUser(user);
        post.setCategory(category);

        SlugUtils slug = new SlugUtils();
        post.setSlug(slug.makeSlug(post.getTitle()));

        // up ảnh
        if (image != null && !image.isEmpty()) {
            try {
                Map uploadResult = cloudinaryService.uploadFile(image);

                post.setImageUrl(uploadResult.get("secure_url").toString());
                post.setImagePublicId(uploadResult.get("public_id").toString());

            } catch (Exception e) {
                throw new RuntimeException("Upload image failed");
            }
        }

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

//    @Override
//    public List<PostDtoResponse> findAllPostByCategory(String category) {
//
//        List<Post> posts = postRepository
//                .findAllPostByCategory_Name(category);
//
//        if (posts.isEmpty()) {
//            throw new AppException(ErrorCode.POST_IS_EMPTY);
//        }
//
//        return posts.stream()
//                .map(postMapper::toResponse)
//                .toList();
//    }

    @Override
    public Page<PostDtoResponse> findAllPostByCategory(String category, int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Post> postPage = postRepository
                .findByCategory_Name(category, pageable);

        if (postPage.isEmpty()) {
            throw new AppException(ErrorCode.POST_IS_EMPTY);
        }

        return postPage.map(postMapper::toResponse);
    }


}
