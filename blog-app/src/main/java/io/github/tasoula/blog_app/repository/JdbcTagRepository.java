package io.github.tasoula.blog_app.repository;

import io.github.tasoula.blog_app.repository.interfaces.TagRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void updatePostTags(UUID postId, List<String> tags) {

        // todo переделать со сравнением двух списков тэгов
        // 1. Удаляем существующие связи для данного поста
        String deleteSql = "DELETE FROM t_sv_post_tag WHERE post_id = ?";
        jdbcTemplate.update(deleteSql, postId);

        for (String tagName : tags) {
            UUID tagId = getOrCreateTagId(tagName);

            // 3. Создаем связь между постом и тегом
            String insertSql = "INSERT INTO t_sv_post_tag (post_id, tag_id, id) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, postId, tagId, UUID.randomUUID());
        }
    }

    private UUID getOrCreateTagId(String tagName) {
        List<UUID> tagIds = jdbcTemplate.queryForList("SELECT id FROM t_tags WHERE name = ?", UUID.class, tagName);

        if (!tagIds.isEmpty()) {
            return tagIds.getFirst(); // Тег существует, возвращаем его ID
        } else {
            // 2. Тег не существует, создаем его
            UUID newTagId = UUID.randomUUID();
            String insertSql = "INSERT INTO t_tags (id, name) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, newTagId, tagName);
            return newTagId;
        }
    }

}
