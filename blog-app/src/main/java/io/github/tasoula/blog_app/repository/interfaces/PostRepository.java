package io.github.tasoula.blog_app.repository.interfaces;

import io.github.tasoula.blog_app.dto.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PostRepository {
    Post findById(UUID id);
    Page<Post> findByTags(List<String> tagName, Pageable pageable);

    UUID create(Post post);
    void update(Post post);
    void delete(UUID id);
    void updateLikes(UUID id, int i);
}
