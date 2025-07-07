package io.github.tasoula.repository;

import io.github.tasoula.dto.Post;
import io.github.tasoula.repository.interfaces.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcPostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<Post> findByTags(List<String> tagName, Pageable pageable) {
        String sql = """ 
                SELECT p.id, p.title, p.image_url, p.content, p.like_count, p.created_at,
                 (select count(1) from t_comments c where p.id = c.post_id) AS comment_count
                 FROM t_posts p""";

        Object[] params = null;
        String sqlCondition = "";
        if (tagName != null && !tagName.isEmpty()) {
            params = tagName.toArray();

            sqlCondition = """ 
                      INNER JOIN t_sv_post_tag pt ON p.id = pt.post_id
                     INNER JOIN t_tags t ON pt.tag_id = t.id
                     WHERE t.name in (%s)""";

            String inClause = String.join(",", java.util.Collections.nCopies(tagName.size(), "?"));
            sqlCondition = String.format(sqlCondition, inClause);
        }

        sql += sqlCondition + " ORDER BY p.created_at DESC" + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();

        List<Post> result = jdbcTemplate.query(sql,
                (rs, rowNum) -> new Post(
                        (UUID) rs.getObject("id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getString("content"),
                        rs.getLong("like_count"),
                        rs.getInt("comment_count"),
                        (Timestamp) rs.getObject("created_at")
                ), params);

        for (Post post : result) {
            String tagSql = """
                    SELECT t.name FROM t_tags t
                     INNER JOIN t_sv_post_tag pt ON t.id = pt.tag_id
                     WHERE pt.post_id = ?""";

            List<String> tags = jdbcTemplate.query(tagSql, (rs, rowNum) -> rs.getString("name"), post.getId());

            post.setTags(tags);
        }

        int total = jdbcTemplate.queryForObject("select count(1) from t_posts p " + sqlCondition, Integer.class, params);

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Post findById(UUID id) {
        return jdbcTemplate.queryForObject(
                "select id, title, image_url, content, like_count, created_at from t_posts where id = ?",
                (rs, rowNum) -> new Post(
                        (UUID) rs.getObject("id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getString("content"),
                        rs.getLong("like_count"),
                        (Timestamp) rs.getObject("created_at")
                ), id);
    }

    @Override
    public UUID create(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO public.t_posts (title, content, image_url, like_count) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});

            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getImageUrl());
            ps.setLong(4, post.getLikeCount());
            return ps;
        }, keyHolder);

        return keyHolder.getKeyAs(UUID.class);
    }
}
