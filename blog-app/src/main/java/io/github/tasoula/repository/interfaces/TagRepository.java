package io.github.tasoula.repository.interfaces;

import java.util.List;
import java.util.UUID;

public interface  TagRepository {
    List<String> getTagsByPostId(UUID postId);

    void updatePostTags(UUID postId, List<String> tags);


}
