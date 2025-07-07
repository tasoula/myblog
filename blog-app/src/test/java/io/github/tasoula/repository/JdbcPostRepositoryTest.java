package io.github.tasoula.repository;


import io.github.tasoula.dto.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcPostRepositoryTest {

 /*    @Mock
    private JdbcTemplate jdbcTemplate;

    private JdbcPostRepository jdbcPostRepository;

    @BeforeEach
    void setUp() {
        jdbcPostRepository = new JdbcPostRepository(jdbcTemplate);
    }

    @Test
   void findByTags_WithTags_ReturnsPageOfPosts() throws SQLException {
        // Arrange
        List<String> tagNames = Arrays.asList("tag1", "tag2");
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> expectedPosts = Arrays.asList(
                createPost(UUID.randomUUID(), "title1", "content1", "image1", 10L, 2, new Timestamp(System.currentTimeMillis())),
                createPost(UUID.randomUUID(), "title2", "content2", "image2", 20L, 5, new Timestamp(System.currentTimeMillis()))
        );

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(PostRowMapper.class)))
                .thenReturn(expectedPosts);

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(TagRowMapper.class)))
                .thenReturn(Arrays.asList("tag1", "tag2"));

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(Object[].class)))
                .thenReturn(2); // total count for pagination

        // Act
        Page<Post> actualPage = jdbcPostRepository.findByTags(tagNames, pageable);

        // Assert
        assertNotNull(actualPage);
        assertEquals(expectedPosts.size(), actualPage.getContent().size());
        assertEquals(2, actualPage.getTotalElements());  // Assert total elements
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(PostRowMapper.class));
        verify(jdbcTemplate, times(2)).query(anyString(), any(Object[].class), any(TagRowMapper.class)); // Called for each post
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), any(Object[].class));
    }

    @Test
    void findByTags_WithNoTags_ReturnsPageOfPosts() throws SQLException {
        // Arrange
        List<String> tagNames = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> expectedPosts = Arrays.asList(
                createPost(UUID.randomUUID(), "title1", "content1", "image1", 10L, 2, new Timestamp(System.currentTimeMillis())),
                createPost(UUID.randomUUID(), "title2", "content2", "image2", 20L, 5, new Timestamp(System.currentTimeMillis()))
        );

        when(jdbcTemplate.query(anyString(), any(PostRowMapper.class)))
                .thenReturn(expectedPosts);

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(TagRowMapper.class)))
                .thenReturn(Arrays.asList("tag1", "tag2"));

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class)))
                .thenReturn(2); // total count for pagination

        // Act
        Page<Post> actualPage = jdbcPostRepository.findByTags(tagNames, pageable);

        // Assert
        assertNotNull(actualPage);
        assertEquals(expectedPosts.size(), actualPage.getContent().size());
        assertEquals(2, actualPage.getTotalElements());  // Assert total elements
        verify(jdbcTemplate, times(1)).query(anyString(), any(PostRowMapper.class));
        verify(jdbcTemplate, times(2)).query(anyString(), any(Object[].class), any(TagRowMapper.class)); // Called for each post
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class));
    }


    @Test
    void findById_ExistingId_ReturnsPost() throws SQLException {
        // Arrange
        UUID postId = UUID.randomUUID();
        Post expectedPost = createPost(postId, "title1", "content1", "image1", 10L, null, new Timestamp(System.currentTimeMillis()));

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(PostRowMapper.class)))
                .thenReturn(expectedPost);

        // Act
        Post actualPost = jdbcPostRepository.findById(postId);

        // Assert
        assertNotNull(actualPost);
        assertEquals(expectedPost.getId(), actualPost.getId());
        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(PostRowMapper.class));
    }

    @Test
    void create_ValidPost_ReturnsGeneratedId() throws SQLException {
        // Arrange
        Post postToCreate = new Post(null, "title", "content", "image", 0L, null, new Timestamp(System.currentTimeMillis()));
        UUID generatedId = UUID.randomUUID();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        when(jdbcTemplate.update(any(java.sql.Connection.class), any(java.sql.PreparedStatementCreator.class), any(GeneratedKeyHolder.class)))
                .thenReturn(1); // Indicate that one row was updated
        when(jdbcTemplate.update(any(java.sql.Connection.class), any(java.sql.PreparedStatementCreator.class), any(GeneratedKeyHolder.class)))
                .thenAnswer(invocation -> {
                    GeneratedKeyHolder holder = invocation.getArgument(2);
                    holder.getKeyList().add(Collections.singletonMap("id", generatedId));
                    return 1;
                });

        // Act
        UUID actualId = jdbcPostRepository.create(postToCreate);

        // Assert
        assertNotNull(actualId);
        assertEquals(generatedId, actualId);
        verify(jdbcTemplate, times(1)).update(any(java.sql.Connection.class), any(java.sql.PreparedStatementCreator.class), any(GeneratedKeyHolder.class));
    }


    @Test
    void update_ValidPost_UpdatesPost() {
        // Arrange
        Post postToUpdate = new Post(UUID.randomUUID(), "new title", "new content", "image", 0L, null, new Timestamp(System.currentTimeMillis()));
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1); // Simulate one row updated

        // Act
        jdbcPostRepository.update(postToUpdate);

        // Assert
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void updateLikes_ValidId_UpdatesLikes() {
        // Arrange
        UUID postId = UUID.randomUUID();
        int delta = 5;
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1); // Simulate one row updated

        // Act
        jdbcPostRepository.updateLikes(postId, delta);

        // Assert
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void delete_ValidId_DeletesPost() {
        // Arrange
        UUID postId = UUID.randomUUID();
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1); // Simulate one row updated

        // Act
        jdbcPostRepository.delete(postId);

        // Assert
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    // Helper methods for creating test data and custom RowMappers

    private Post createPost(UUID id, String title, String content, String imageUrl, Long likeCount, Integer commentCount, Timestamp createdAt) {
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setLikeCount(likeCount);
        post.setCommentCount(commentCount);
        post.setCreatedAt(createdAt);
        return post;
    }

    private interface RowMapper<T> {
        T mapRow(ResultSet rs, int rowNum) throws SQLException;
    }


    private class PostRowMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID id = (UUID) rs.getObject("id");
            String title = rs.getString("title");
            String imageUrl = rs.getString("image_url");
            String content = rs.getString("content");
            Long likeCount = rs.getLong("like_count");
            Integer commentCount = rs.getInt("comment_count");
            Timestamp createdAt = (Timestamp) rs.getObject("created_at");

            Post post = new Post(id, title, imageUrl, content, likeCount, commentCount, createdAt);
            return post;
        }
    }


    private class TagRowMapper implements RowMapper<String> {

        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("name");
        }
    }

   */
}
