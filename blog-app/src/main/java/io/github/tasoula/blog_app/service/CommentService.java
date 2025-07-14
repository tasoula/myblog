package io.github.tasoula.blog_app.service;

import io.github.tasoula.blog_app.dto.Comment;
import io.github.tasoula.blog_app.repository.interfaces.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public List<Comment> getPostComments(UUID postId) {
        return repository.getPostComments(postId);
    }

    public void create(UUID postId, Comment comment) {
        repository.create(postId, comment);
    }

    public void update(Comment comment) {
        repository.update(comment);
    }

    public void delete(UUID commentId) {
        repository.delete(commentId);
    }
}
