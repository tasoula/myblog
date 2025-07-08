package io.github.tasoula.repository;

import io.github.tasoula.config.DataSourceTestConfiguration;
import io.github.tasoula.config.WebTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitConfig(classes = {DataSourceTestConfiguration.class, WebTestConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
class JdbcTagRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcTagRepository tagRepository;

    private UUID postId1;
    private UUID postId2;
    private UUID tagId1;
    private UUID tagId2;


    @BeforeEach
    void setUp() {
        tagRepository = new JdbcTagRepository(jdbcTemplate);

        // Очистка таблиц
        jdbcTemplate.execute("DELETE FROM t_sv_post_tag");
        jdbcTemplate.execute("DELETE FROM t_tags");
        jdbcTemplate.execute("DELETE FROM t_posts");

        // Генерация тестовых UUIDs
        postId1 = UUID.randomUUID();
        postId2 = UUID.randomUUID();
        tagId1 = UUID.randomUUID();
        tagId2 = UUID.randomUUID();

        jdbcTemplate.update("INSERT INTO t_posts (id, title) VALUES (?, ?)", postId1, "Test post 1");
        jdbcTemplate.update("INSERT INTO t_posts (id, title) VALUES (?, ?)", postId2, "Test post 2");

        // Добавление тестовых данных
        jdbcTemplate.execute("INSERT INTO t_tags (id, name) VALUES ('" + tagId1 + "', 'java')");
        jdbcTemplate.execute("INSERT INTO t_tags (id, name) VALUES ('" + tagId2 + "', 'spring')");

        jdbcTemplate.execute("INSERT INTO t_sv_post_tag (post_id, tag_id, id) VALUES ('" + postId1 + "', '" + tagId1 + "', '" + UUID.randomUUID() + "')");
        jdbcTemplate.execute("INSERT INTO t_sv_post_tag (post_id, tag_id, id) VALUES ('" + postId1 + "', '" + tagId2 + "', '" + UUID.randomUUID() + "')");

    }

    @Test
    void getTagsByPostId_shouldReturnListOfTagsForGivenPostId() {
        List<String> tags = tagRepository.getTagsByPostId(postId1);

        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertTrue(tags.contains("java"));
        assertTrue(tags.contains("spring"));

        List<String> tagsForNonExistentPost = tagRepository.getTagsByPostId(postId2);
        assertNotNull(tagsForNonExistentPost);
        assertTrue(tagsForNonExistentPost.isEmpty());
    }

    @Test
    void updatePostTags_shouldUpdateTagsForGivenPostId() {
        List<String> newTags = Arrays.asList("kotlin", "gradle");
        tagRepository.updatePostTags(postId1, newTags);

        List<String> updatedTags = tagRepository.getTagsByPostId(postId1);
        assertNotNull(updatedTags);
        assertEquals(2, updatedTags.size());
        assertTrue(updatedTags.contains("kotlin"));
        assertTrue(updatedTags.contains("gradle"));

        // Проверяем, что старые теги удалены
        assertFalse(updatedTags.contains("java"));
        assertFalse(updatedTags.contains("spring"));

        // Проверяем, что в таблице t_tags появились новые теги (kotlin и gradle)
        List<String> allTags = jdbcTemplate.queryForList("SELECT name FROM t_tags", String.class);
        assertTrue(allTags.contains("kotlin"));
        assertTrue(allTags.contains("gradle"));
    }

    @Test
    void updatePostTags_shouldAddTagsWhenNotExist() {
        List<String> newTags = Arrays.asList("kotlin", "gradle");

        tagRepository.updatePostTags(postId1, newTags);

        List<String> updatedTags = tagRepository.getTagsByPostId(postId1);
        assertNotNull(updatedTags);
        assertEquals(2, updatedTags.size());
        assertTrue(updatedTags.contains("kotlin"));
        assertTrue(updatedTags.contains("gradle"));

        List<String> allTags = jdbcTemplate.queryForList("SELECT name FROM t_tags", String.class);
        assertTrue(allTags.contains("kotlin"));
        assertTrue(allTags.contains("gradle"));
    }

    @Test
    void updatePostTags_shouldWorkCorrectlyWithEmptyTagList() {
        tagRepository.updatePostTags(postId1, List.of());
        List<String> updatedTags = tagRepository.getTagsByPostId(postId1);

        assertNotNull(updatedTags);
        assertTrue(updatedTags.isEmpty());
    }

    @Test
    void updatePostTags_shouldAddAndRemoveTagsCorrectly() {
        List<String> newTags = Arrays.asList("java", "kotlin");

        tagRepository.updatePostTags(postId1, newTags);

        List<String> updatedTags = tagRepository.getTagsByPostId(postId1);
        assertNotNull(updatedTags);
        assertEquals(2, updatedTags.size());
        assertTrue(updatedTags.contains("java"));
        assertTrue(updatedTags.contains("kotlin"));
        assertFalse(updatedTags.contains("spring"));
    }
}
