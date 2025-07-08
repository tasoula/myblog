package io.github.tasoula.controller;

import io.github.tasoula.config.DataSourceTestConfiguration;
import io.github.tasoula.config.WebTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceTestConfiguration.class, WebTestConfiguration.class})
class PostFeedControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String expectedTitle = "Заголовок моего поста";
    private final String expectedImageSrc = "/posts/images/"; // Или любое другое ожидаемое значение
    private final String expectedContent = "Краткое описание моего поста...";
    private final int expectedLikeCount = 3;
    private final int expectedCommentCount = 1;

    private UUID postId ;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.execute("DELETE FROM t_comments");
        jdbcTemplate.execute("DELETE FROM t_posts");
        jdbcTemplate.execute("DELETE FROM t_tags");
        jdbcTemplate.execute("DELETE FROM t_sv_post_tag");

        postId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_posts (id, title, content, image_url, like_count) VALUES (?, ?, ?, ?, ?)",
                postId, expectedTitle, expectedContent, "12345_tes_image.jpg", expectedLikeCount);

        UUID tagId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_tags (id, name) VALUES (?, ?)",tagId, "tag");

        UUID postTagID = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_sv_post_tag (post_id, tag_id, id) VALUES (?, ?, ?)",postId, tagId, postTagID);

        UUID commentId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_comments (id, post_id, content) VALUES (?, ?, ?)", commentId, postId, "comment");
    }

    @Test
    void show() throws Exception {
        String expectedLikeCountStr = "👍 " + String.valueOf(expectedLikeCount);
        String expectedCommentCountStr = "✉ " + String.valueOf(expectedCommentCount);

        mockMvc.perform(get("/posts")
                        .param("search", "#tag")
                        .param("pageSize","10")
                        .param("pageNumber", "0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(model().attributeExists("search"))
                .andExpect(xpath("//table/tr").nodeCount(2))  //У меня в таблице один пост
                .andExpect(xpath("//table/tr[2]/td/a/h2/text()").string(expectedTitle))
                .andExpect(xpath("//table/tr[2]/td/p/a/img/@src").string(expectedImageSrc  + postId))
                 .andExpect(xpath("//table/tr[2]/td/p[2]/text()").string(expectedContent))
                 .andExpect(xpath("//table/tr[2]/td/p[3]/span/text()").string(expectedLikeCountStr))
                 .andExpect(xpath("//table/tr[2]/td/p[3]/span[2]/text()").string(expectedCommentCountStr));
    }

    @Test
    void newPost() throws Exception {
        mockMvc.perform(get("/posts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("new-post"));
    }

    @Test
    void create() throws Exception {
        // Создаем MockMultipartFile для имитации загруженного файла
        MockMultipartFile image = new MockMultipartFile(
                "image",                 // Имя параметра, как в контроллере
                "test.jpg",            // Оригинальное имя файла
                "image/jpeg",          // Тип содержимого файла
                "some image data".getBytes() // Содержимое файла (можно заменить реальными данными)
        );
        mockMvc.perform(MockMvcRequestBuilders.multipart("/posts")
                        .file(image)
                        .param("title", "Title")
                        .param("tags", "#tag")
                        .param("content", "Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }
}