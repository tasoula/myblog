package io.github.tasoula.blog_app.repository;


import io.github.tasoula.blog_app.dto.Post;
import io.github.tasoula.blog_app.repository.JdbcPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.util.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

//@SpringJUnitConfig(classes = {DataSourceConfiguration.class, WebConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test") // Активируем профиль "test"
class JdbcPostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcPostRepository postRepository;

    private final UUID postId1 = UUID.randomUUID();
    private final String expectedTitle = "Post 1";
    private  final String expectedImage = "image1.jpg";
    private final String expectedContenet = "Content 1";
    private final int expentedLikes = 10;

    private UUID postId2;

    @BeforeEach
    void setUp() {
        postRepository = new JdbcPostRepository(jdbcTemplate);
        // Очистка базы данных
        jdbcTemplate.execute("DELETE FROM t_sv_post_tag");
        jdbcTemplate.execute("DELETE FROM t_tags");
        jdbcTemplate.execute("DELETE FROM t_posts");

        UUID postId2 = UUID.randomUUID();
        UUID postId3 = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_posts (id, title, image_url, content, like_count) VALUES (?, ?, ?, ?, ?)",
                postId1 , expectedTitle, expectedImage, expectedContenet, expentedLikes);
        jdbcTemplate.update("INSERT INTO t_posts (id, title, image_url, content, like_count) VALUES (?, ?, ?, ?, ?)",
                postId2 , "Post 2", "image2.jpg", "Content 2", 5);
        jdbcTemplate.update("INSERT INTO t_posts (id, title, image_url, content, like_count) VALUES (?, ?, ?, ?, ?)",
                postId3 , "Post 3", "image3.jpg", "Content 3", 6);

        UUID tagId1 = UUID.randomUUID();
        UUID tagId2 = UUID.randomUUID();

        jdbcTemplate.execute("INSERT INTO t_tags (id, name) VALUES ('" + tagId1 + "', 'java')");
        jdbcTemplate.execute("INSERT INTO t_tags (id, name) VALUES ('" + tagId2 + "', 'spring')");

        jdbcTemplate.update("INSERT INTO t_sv_post_tag (post_id, tag_id, id) VALUES (?, ?, ?)", postId1, tagId1, UUID.randomUUID());
        jdbcTemplate.update("INSERT INTO t_sv_post_tag (post_id, tag_id, id) VALUES (?, ?, ?)", postId1, tagId2, UUID.randomUUID());
        jdbcTemplate.update("INSERT INTO t_sv_post_tag (post_id, tag_id, id) VALUES (?, ?, ?)", postId2, tagId1, UUID.randomUUID());
    }

    @Test
    void findById_shouldReturnPostIfExists() {
        Post post = postRepository.findById(postId1);
        assertNotNull(post);
        assertEquals(postId1, post.getId());
    }

    @Test
    void findById_shouldReturnNullIfPostDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> postRepository.findById(nonExistentId));
    }

    @Test
    void findByTags_shouldReturnPostsWithGivenTags() {
        Pageable pageable = PageRequest.of(0, 10);
        List<String> tags = List.of("java", "spring");

        Page<Post> posts = postRepository.findByTags(tags, pageable);

        assertNotNull(posts);
        assertEquals(2, posts.getTotalElements());
        assertEquals(1, posts.getTotalPages());
        assertEquals(2, posts.getContent().size());
        assertEquals("Post 2", posts.getContent().get(0).getTitle());
        assertEquals("Post 1", posts.getContent().get(1).getTitle());
        assertEquals(1, posts.getContent().get(0).getTags().size());
        assertTrue(posts.getContent().get(0).getTags().contains("java"));
        assertEquals(2, posts.getContent().get(1).getTags().size());
        assertTrue(posts.getContent().get(1).getTags().contains("java"));
        assertTrue(posts.getContent().get(1).getTags().contains("spring"));
    }

    @Test
    void findByTags_shouldReturnEmptyPage_whenNoPostsMatchTags() {
        Pageable pageable = PageRequest.of(0, 10);
        List<String> tags = List.of("nonexistent");

        Page<Post> posts = postRepository.findByTags(tags, pageable);

        assertNotNull(posts);
        assertEquals(0, posts.getTotalElements());
        assertEquals(0, posts.getTotalPages());
        assertTrue(posts.getContent().isEmpty());
    }

    @Test
    void findByTags_shouldReturnAllPosts_whenTagsIsNull() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Post> posts = postRepository.findByTags(null, pageable);

        assertNotNull(posts);
        assertEquals(3, posts.getTotalElements());
        assertEquals(1, posts.getTotalPages());
        assertEquals(3, posts.getContent().size());
    }

    @Test
    void findByTags_shouldReturnAllPosts_whenTagsIsEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        List<String> tags = List.of();

        Page<Post> posts = postRepository.findByTags(tags, pageable);

        assertNotNull(posts);
        assertEquals(3, posts.getTotalElements());
        assertEquals(1, posts.getTotalPages());
        assertEquals(3, posts.getContent().size());
    }

    @Test
    void findByTags_withPagination() {
        Pageable pageable = PageRequest.of(1, 1); // page 1, size 1

        Page<Post> posts = postRepository.findByTags(null, pageable);

        assertNotNull(posts);
        assertEquals(3, posts.getTotalElements());
        assertEquals(3, posts.getTotalPages());
        assertEquals(1, posts.getContent().size());

        // Ensure the correct post is returned on the second page
        assertEquals("Post 2", posts.getContent().get(0).getTitle());
    }

    @Test
    void create_shouldCreateNewPost() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("This is a test content.");
        post.setTags(List.of("tag1", "tag2"));
        post.setImageUrl("http://example.com/image.jpg");

        UUID postId = postRepository.create(post);

        assertNotNull(postId);

        // Проверка, что пост действительно был создан
        Post retrievedPost = jdbcTemplate.queryForObject(
                "select id, title, image_url, content, like_count, created_at from t_posts where id = ?",
                (rs, rowNum) -> new Post(
                        (UUID) rs.getObject("id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getString("content"),
                        rs.getLong("like_count"),
                        (Timestamp) rs.getObject("created_at")
                ), postId);

        assertNotNull(retrievedPost);
        assertEquals("Test Post", retrievedPost.getTitle());
        assertEquals("This is a test content.", retrievedPost.getContent());
        assertEquals("http://example.com/image.jpg", retrievedPost.getImageUrl());
        assertEquals(0, retrievedPost.getLikeCount());
    }

    @Test
    void update_shouldUpdateExistingPost() {
        // Сначала создаем пост
        Post initialPost = new Post();
        initialPost.setTitle("Initial Title");
        initialPost.setContent("Initial Content");
        initialPost.setImageUrl("initial_url");
        UUID postId = postRepository.create(initialPost);

        // Обновляем пост
        Post updatedPost = new Post();
        updatedPost.setId(postId);
        updatedPost.setTitle("Updated Title");
        updatedPost.setContent("Updated Content");
        postRepository.update(updatedPost);

        // Проверяем, что пост был обновлен
        Post retrievedPost = jdbcTemplate.queryForObject("select id, title, image_url, content, like_count, created_at from t_posts where id = ?",
                (rs, rowNum) -> new Post(
                        (UUID) rs.getObject("id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getString("content"),
                        rs.getLong("like_count"),
                        (Timestamp) rs.getObject("created_at")
                ), postId);

        assertNotNull(retrievedPost);
        assertEquals("Updated Title", retrievedPost.getTitle());
        assertEquals("Updated Content", retrievedPost.getContent());
        assertEquals("initial_url", retrievedPost.getImageUrl());
    }

    @Test
    void delete_shouldDeletePost() {
        // Создаем пост
        Post initialPost = new Post();
        initialPost.setTitle("Title to Delete");
        initialPost.setContent("Content to Delete");
        initialPost.setImageUrl("delete_url");
        initialPost.setLikeCount(5);
        UUID postId = postRepository.create(initialPost);

        // Удаляем пост
        postRepository.delete(postId);

        // Проверяем, что пост был удален
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM t_posts WHERE id = ?", Integer.class, postId);
        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    void updateLikes_shouldUpdateLikeCount() {
        // Создаем пост
        Post initialPost = new Post();
        initialPost.setTitle("Title for Likes");
        initialPost.setContent("Content for Likes");
        initialPost.setImageUrl("likes_url");
        initialPost.setLikeCount(5);
        UUID postId = postRepository.create(initialPost);

        // Обновляем количество лайков
        postRepository.updateLikes(postId, 3);

        // Проверяем, что количество лайков было обновлено
        Long updatedLikeCount = jdbcTemplate.queryForObject("SELECT like_count FROM t_posts WHERE id = ?", Long.class, postId);
        assertNotNull(updatedLikeCount);
        assertEquals(8, updatedLikeCount);

        // Проверяем отрицательное значение delta
        postRepository.updateLikes(postId, -5);
        updatedLikeCount = jdbcTemplate.queryForObject("SELECT like_count FROM t_posts WHERE id = ?", Long.class, postId);
        assertNotNull(updatedLikeCount);
        assertEquals(3, updatedLikeCount);
    }
}
