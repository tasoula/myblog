package io.github.tasoula.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {
    private final static int PREVIEW_LENGTH = 200;

    private UUID id;    //post, posts, new-post
    private String title; //post, posts, new-post
    private String imageUrl; //post, posts, new-post
    private String content; //post, posts (превью), new-post
    private long likeCount; //post, posts
    private List<String> tags; //post, posts, new-post
    private List<Comment> comments; //post
    private Integer commentsCount; //posts
    private Timestamp createdAt;

    public Post(UUID id, String title, String imageUrl, String content, long likesCount, int commentsCount, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.likeCount = likesCount;
        this.comments = new ArrayList<>();
        this.commentsCount = commentsCount;

        this.tags = new ArrayList<>();
        this.createdAt = createdAt;
    }

    public Post(UUID id, String title, String imageUrl, String content, long likesCount, Timestamp created_at) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.likeCount = likesCount;
        this.comments = new ArrayList<>();
        this.createdAt = created_at;
    }

    public String getTextPreview() {
        int delimiterIndex = content.indexOf("\r\n");
        if (delimiterIndex != -1)
            return content.substring(0, Math.min(PREVIEW_LENGTH, delimiterIndex)) + "...";

        if (content.length() > PREVIEW_LENGTH)
            return content.substring(0, PREVIEW_LENGTH) + "...";

        return content;
    }

    public List<String> getTextParts() {
        return Arrays.stream(content.split("\\r?\\n")).collect(Collectors.toList());
    }

    public String getTagsAsText() {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        return tags.stream().map(tag -> "#" + tag).collect(Collectors.joining(" "));
    }
}
