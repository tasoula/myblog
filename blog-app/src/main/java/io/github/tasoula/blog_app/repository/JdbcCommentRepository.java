package io.github.tasoula.blog_app.repository;

import io.github.tasoula.blog_app.dto.Comment;
import io.github.tasoula.blog_app.repository.interfaces.CommentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void create(UUID postId, Comment comment) {
        jdbcTemplate.update("INSERT INTO t_comments (post_id, content) VALUES (?, ?)",
                postId,
                comment.getText());
    }

    @Override
    public void update(Comment comment) {
        jdbcTemplate.update("UPDATE t_comments SET content = ? WHERE id = ?",
                comment.getText(),
                comment.getId());
    }

    @Override
    public void delete(UUID id) {
        jdbcTemplate.update("DELETE FROM t_comments WHERE id = ?", id);
    }

    @Override
    public List<Comment> getPostComments(UUID postId) {

        return jdbcTemplate.query(
                "select id, content, created_at from t_comments where post_id = ? ORDER BY created_at",
                (rs, rowNum) -> new Comment(
                        (UUID) rs.getObject("id"),
                        rs.getString("content"),
                        (Timestamp) rs.getObject("created_at")),
                postId);
    }
}
