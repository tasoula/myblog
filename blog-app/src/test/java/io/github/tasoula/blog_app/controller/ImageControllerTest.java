package io.github.tasoula.blog_app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
//@ContextConfiguration(classes = {DataSourceConfiguration.class, WebConfiguration.class})
@ActiveProfiles("test")
class ImageControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${upload.images.dir}")
    private String uploadDir;

    private UUID postId;

    @BeforeEach
    void setUp() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.execute("DELETE FROM t_posts");

        String filename = "12345_test_image.jpg";

        postId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_posts (id, title, image_url) VALUES (?, ?, ?)",
                postId, "title", filename);

        byte[] contentBytes = "Это наша картинка".getBytes();
        InputStream inputStream = new ByteArrayInputStream(contentBytes);

        // 2. Сохранить файл на диск
        Path filePath = Paths.get(uploadDir, filename);
        Files.copy(inputStream, filePath);
    }

    @Test
    void image() throws Exception {
        mockMvc.perform(get("/posts/images/{id}", postId.toString()))
               .andExpect(status().isOk())
         //       .andExpect(content().contentType("text/html;charset=UTF-8"))
             //   .andExpect(view().name("post"))
             //   .andExpect(model().attributeExists("post"))
        ;
    }
}