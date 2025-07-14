package io.github.tasoula.blog_app.repository.interfaces;

import java.util.UUID;

public interface ImageRepository {
    String getPostImage(UUID postId);
    void updatePostImage(UUID postId, String filename);
}
