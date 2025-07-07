package io.github.tasoula.repository;

import io.github.tasoula.repository.interfaces.ImageRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JdbcImageRepository implements ImageRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcImageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getPostImage(UUID postId) {
        return jdbcTemplate.queryForObject("select image_url from t_posts where id = ?", String.class, postId);
    }

    @Override
    public void updatePostImage(UUID postId, String filename) {
        jdbcTemplate.update("UPDATE t_posts SET image_url = ? WHERE id = ?", filename, postId);
    }
}
