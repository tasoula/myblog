package io.github.tasoula.blog_app.repository;


import io.github.tasoula.blog_app.dto.Comment;
import io.github.tasoula.blog_app.repository.JdbcCommentRepository;
import io.github.tasoula.blog_app.repository.interfaces.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Активируем профиль "test"
class JdbcCommentRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CommentRepository commentRepository;
    private UUID postId;

    @BeforeEach
    void setUp() {
        commentRepository = new JdbcCommentRepository(jdbcTemplate);

        // Очистка таблицы комментариев
        jdbcTemplate.execute("DELETE FROM t_comments");

        // Создание UUID для post_id
        postId = UUID.randomUUID();
        jdbcTemplate.execute("DELETE FROM t_comments");
        jdbcTemplate.execute("DELETE FROM t_posts");

        postId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_posts (id, title) VALUES (?, ?)", postId, "Test post");

        // Добавление тестовых данных (комментариев)
        jdbcTemplate.execute("INSERT INTO t_comments (id, post_id, content, created_at) VALUES (UUID('" + UUID.randomUUID() + "'), '" + postId + "', 'Отличный пост!', NOW())");
        jdbcTemplate.execute("INSERT INTO t_comments (id, post_id, content, created_at) VALUES (UUID('" + UUID.randomUUID() + "'), '" + postId + "', 'Согласен!', NOW())");
    }

    @Test
    void create_shouldAddCommentToDatabase() {
        Comment comment = new Comment(UUID.randomUUID(), "Интересно!", null);

        commentRepository.create(postId, comment);

        List<Comment> comments = commentRepository.getPostComments(postId);

        boolean found = false;
        for (Comment c : comments) {
            if (c.getText().equals("Интересно!")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    @Test
    void update_shouldUpdateCommentInDatabase() {
        // Предполагаем, что у нас есть хотя бы один комментарий в базе данных, полученный из setUp()
        List<Comment> initialComments = commentRepository.getPostComments(postId);
        assertFalse(initialComments.isEmpty(), "Database should contain comments after setUp()");

        Comment commentToUpdate = initialComments.getFirst();

        String newContent = "Обновленный комментарий";
        Comment updatedComment = new Comment(commentToUpdate.getId(), newContent, null);
        commentRepository.update(updatedComment);

        List<Comment> updatedComments = commentRepository.getPostComments(postId);

        boolean found = false;
        for (Comment c : updatedComments) {
            if (c.getId().equals(commentToUpdate.getId()) && c.getText().equals(newContent)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void delete_shouldRemoveCommentFromDatabase() {
        // Предполагаем, что у нас есть хотя бы один комментарий в базе данных, полученный из setUp()
        List<Comment> initialComments = commentRepository.getPostComments(postId);
        assertFalse(initialComments.isEmpty(), "Database should contain comments after setUp()");

        Comment commentToDelete = initialComments.getFirst();
        UUID idToDelete = commentToDelete.getId();

        commentRepository.delete(idToDelete);

        List<Comment> commentsAfterDeletion = commentRepository.getPostComments(postId);

        boolean found = false;
        for (Comment c : commentsAfterDeletion) {
            if (c.getId().equals(idToDelete)) {
                found = true;
                break;
            }
        }

        assertFalse(found);
    }

    @Test
    void getPostComments_shouldReturnCommentsForPost() {
        List<Comment> comments = commentRepository.getPostComments(postId);

        assertNotNull(comments);
        assertEquals(2, comments.size()); // Ожидаем 2 комментария, добавленных в setUp

        for (Comment comment : comments) {
            assertNotNull(comment.getText());
        }
    }
}


