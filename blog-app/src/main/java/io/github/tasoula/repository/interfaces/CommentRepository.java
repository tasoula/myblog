package io.github.tasoula.repository.interfaces;

import io.github.tasoula.dto.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository {
    List<Comment> getPostComments(UUID id);
    void create(UUID postId, Comment comment);
    void update(Comment comment);
    void delete(UUID id);
}
