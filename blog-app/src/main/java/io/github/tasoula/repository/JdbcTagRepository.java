package io.github.tasoula.repository;

import io.github.tasoula.repository.interfaces.TagRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JdbcTagRepository implements TagRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getTagsByPostId(UUID postId) {
        String sql = "SELECT t.name FROM t_tags t JOIN t_sv_post_tag pst ON t.id = pst.tag_id WHERE pst.post_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, postId);
    }
}
