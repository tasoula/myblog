package io.github.tasoula.repository.interfaces;

import io.github.tasoula.dto.Post;
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
}
