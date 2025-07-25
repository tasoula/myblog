package io.github.tasoula.blog_app.service;

import io.github.tasoula.blog_app.dto.Post;
import io.github.tasoula.blog_app.repository.interfaces.PostRepository;
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

    public void update(UUID id, String title, MultipartFile image, String tags, String content) {
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setContent(content);
        postRepository.update(post); // Сохранить пост в базу

        imageService.updatePostImage(id, image);
        tagService.updatePostTags(id, tags);
    }

    public void delete(UUID id) {
        imageService.deletePostImage(id);
        postRepository.delete(id);
    }

    public void addLike(UUID id, boolean like) {
        postRepository.updateLikes(id, like ? 1 : -1);
    }

}