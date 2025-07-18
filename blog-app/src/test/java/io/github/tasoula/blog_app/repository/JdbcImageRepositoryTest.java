package io.github.tasoula.blog_app.repository;

import io.github.tasoula.blog_app.repository.interfaces.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Активируем профиль "test"
public class JdbcImageRepositoryTest {

    //todo в рабочей папке с картинками создаются тестовые картинки и не удаляются
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ImageRepository imageRepository;  // Inject the repository

    private UUID testPostId;
    private String initialImageUrl = "initial_image.jpg";

    @BeforeEach
    void setUp() {

        testPostId = UUID.randomUUID();

        jdbcTemplate.execute("DELETE FROM t_posts");

        jdbcTemplate.update("INSERT INTO t_posts (id, title, image_url) VALUES (?, ?, ?)", testPostId, "Test post", initialImageUrl);

    }

    @Test
    void getPostImage_shouldReturnImageUrlForExistingPost() {
        String imageUrl = imageRepository.getPostImage(testPostId);

        assertNotNull(imageUrl);
        assertEquals(initialImageUrl, imageUrl);
    }

    @Test
    void getPostImage_shouldThrowExceptionForNonExistingPost() {
        UUID nonExistingPostId = UUID.randomUUID();
        assertThrows(EmptyResultDataAccessException.class, () -> imageRepository.getPostImage(nonExistingPostId));
    }


    @Test
    void updatePostImage_shouldUpdateImageUrlForExistingPost() {
        String newImageUrl = "updated_image.png";
        imageRepository.updatePostImage(testPostId, newImageUrl);

        String updatedImageUrl = jdbcTemplate.queryForObject("SELECT image_url FROM t_posts WHERE id = ?", String.class, testPostId);

        assertNotNull(updatedImageUrl);
        assertEquals(newImageUrl, updatedImageUrl);
    }
}
