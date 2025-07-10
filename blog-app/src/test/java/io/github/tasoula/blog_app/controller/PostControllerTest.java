package io.github.tasoula.blog_app.controller;

import io.github.tasoula.blog_app.config.DataSourceConfiguration;
import io.github.tasoula.blog_app.config.WebConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceConfiguration.class, WebConfiguration.class})
@ActiveProfiles("test")
class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private UUID postId;
    private UUID tagId;
    private UUID postTagID;
    private UUID commentId;
    private final String expectedTitle = "Заголовок моего поста";
    private final String expectedTags = "tag";
    private final String expectedComment = "comment";
    private final String expectedImageSrc = "/posts/images/"; // Или любое другое ожидаемое значение
    private final String expectedContent = "Краткое описание моего поста...";
    private final int expectedLikeCount = 3;
    private final int expectedCommentCount = 1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.execute("DELETE FROM t_comments");
        jdbcTemplate.execute("DELETE FROM t_posts");
        jdbcTemplate.execute("DELETE FROM t_tags");
        jdbcTemplate.execute("DELETE FROM t_sv_post_tag");

        postId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_posts (id, title, content, image_url, like_count) VALUES (?, ?, ?, ?, ?)",
                postId, expectedTitle, expectedContent, "12345_test_image.jpg", expectedLikeCount);

        tagId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_tags (id, name) VALUES (?, ?)",tagId, expectedTags);

        postTagID = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_sv_post_tag (post_id, tag_id, id) VALUES (?, ?, ?)",postId, tagId, postTagID);

        commentId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_comments (id, post_id, content) VALUES (?, ?, ?)", commentId, postId, expectedComment);
    }

    @Test
    void show() throws Exception {
         mockMvc.perform(get("/posts/{id}", postId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                  .andExpect(xpath("//table/tr").nodeCount(6))
                .andExpect(xpath("//table/tr[2]/td/h2/text()").string(expectedTitle))
                  .andExpect(xpath("//table/tr[2]/td/p/img/@src").string(expectedImageSrc + postId))
                .andExpect(xpath("//table/tr[2]/td/p[2]/form/span/text()").string(String.valueOf(expectedLikeCount)))
                .andExpect(xpath("//table/tr[2]/td/p[2]/form/span[2]/text()").string("комментарии " + String.valueOf(expectedCommentCount)))
               .andExpect(xpath("//table/tr[2]/td/p[3]/span/text()").string("#"+expectedTags +" "))
                 .andExpect(xpath("//table/tr[3]/td/text()").string(expectedContent))
                 .andExpect(xpath("//table/tr[5]").nodeCount(1))
                 .andExpect(xpath("//table/tr[5]/td/form/span/text()").string(expectedComment)) ;
    }

    @Test
    void save() throws Exception {
        // Создаем MockMultipartFile для имитации загруженного файла
        MockMultipartFile image = new MockMultipartFile(
                "image",                 // Имя параметра, как в контроллере
                "test.jpg",            // Оригинальное имя файла
                "image/jpeg",          // Тип содержимого файла
                "some image data".getBytes() // Содержимое файла (можно заменить реальными данными)
        );
        mockMvc.perform(MockMvcRequestBuilders.multipart("/posts")
                        .file(image)
                        .param("id", postId.toString())
                        .param("title", "Title")
                        .param("tags", "#tag")
                        .param("content", "Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }


    @Test
    void edit() throws Exception {
       mockMvc.perform(get("/posts/{post_id}/edit", postId.toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(view().name("new-post"));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post("/posts/{post_id}/delete", postId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void like() throws Exception {
        mockMvc.perform(post("/posts/{post_id}/like", postId.toString()).param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/"+postId));
    }
}