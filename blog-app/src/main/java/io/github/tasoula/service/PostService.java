package io.github.tasoula.service;

import io.github.tasoula.dto.Post;
import io.github.tasoula.repository.interfaces.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public Post getPost(UUID id) {
        Post post = postRepository.findById(id);
        post.setComments(commentService.getPostComments(id));
        post.setTags(tagService.getTagsByPostId(id));
        return post;
    }

    public Page<Post> findByTags(String tagName, Pageable pageable) {
        return postRepository.findByTags(tagService.parseTags(tagName), pageable);
    }
}