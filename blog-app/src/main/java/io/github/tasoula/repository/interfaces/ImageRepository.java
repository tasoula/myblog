package io.github.tasoula.repository.interfaces;

import java.util.UUID;

public interface ImageRepository {
    String getPostImage(UUID postId);
}
