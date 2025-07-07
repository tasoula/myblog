package io.github.tasoula.service;

import io.github.tasoula.dto.Comment;
import io.github.tasoula.repository.interfaces.CommentRepository;
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
