package io.github.tasoula.service;

import io.github.tasoula.dto.Post;
import io.github.tasoula.repository.interfaces.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class PostService {
    private final PostRepository postRepository;

    private final CommentService commentService;
    private final ImageService imageService;
    private final TagService tagService;

    public PostService(PostRepository postRepository, CommentService commentService, ImageService imageService, TagService tagService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.imageService = imageService;
        this.tagService = tagService;
    }

    public Page<Post> findByTags(String tagName, Pageable pageable) {
        return postRepository.findByTags(tagService.parseTags(tagName), pageable);
    }

    public Post getPost(UUID id) {
        Post post = postRepository.findById(id);
        post.setComments(commentService.getPostComments(id));
        post.setTags(tagService.getTagsByPostId(id));
        return post;
    }

    public void create(String title, MultipartFile image, String tags, String content) {
        try {
            Post post = new Post();
            post.setTitle(title);
            post.setTags(tagService.parseTags(tags));
            post.setContent(content);

            String filename = imageService.saveToDisc(image);
            post.setImageUrl(filename);

            UUID postId = postRepository.create(post);


            if (postId != null) {
                post.setId(postId);
                tagService.updatePostTags(postId, tags);
            }

        } catch (IOException e) {
            log.error("Произошла ошибка: {}", e.getMessage(), e);
        }
    }

}