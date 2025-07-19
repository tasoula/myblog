package io.github.tasoula.blog_app.service;

import io.github.tasoula.blog_app.repository.interfaces.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TagService {
    private final TagRepository repository;

    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public List<String> getTagsByPostId(UUID postId){
        return repository.getTagsByPostId(postId);
    }

    public void updatePostTags(UUID postId, String tags) {
        repository.updatePostTags(postId, parseTags(tags));
    }

    public List<String> parseTags(String input) {
        List<String> tags = new ArrayList<>();

        if (input == null || input.trim().isEmpty()) {
            return tags;
        }

        Pattern pattern = Pattern.compile("#([\\wа-яА-Я0-9_-]+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String tag = matcher.group(1); // Получаем только содержимое захватывающей группы (без #)
            tags.add(tag);
        }

        return tags;
    }
}
