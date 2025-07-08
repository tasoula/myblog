package io.github.tasoula.controller;

import io.github.tasoula.config.DataSourceConfiguration;
import io.github.tasoula.config.WebConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceTestConfiguration.class, WebTestConfiguration.class})*/
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfiguration.class, DataSourceConfiguration.class})
@ActiveProfiles("test") // Активируем профиль "test"
public class CommentControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID postId;
    private  UUID commentId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация Mockito

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.execute("DELETE FROM t_comments");
        jdbcTemplate.execute("DELETE FROM t_posts");

       postId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_posts (id, title) VALUES (?, ?)",  postId, "заголовок поста");

        commentId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_comments (id, post_id, content) VALUES (?, ?, ?)", commentId, postId, "comment");
    }


    @Test
    void addComment() throws Exception {

        mockMvc.perform(post("/posts/{id}/comments", postId.toString()) // Подставляем ID в URL
                        .param("text", "New comment")) // Передаем поле 'text' объекта Comment как параметр
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId.toString())); // Проверяем правильный URL перенаправления
    }

    @Test
    void editComment() throws Exception {
        mockMvc.perform(post("/posts/{post_id}/comments/{comment_id}", postId.toString(), commentId.toString())
                        .param("comment_id", commentId.toString())
                        .param("text", "New comment")) // Передаем поле 'text' объекта Comment как параметр
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId.toString())); // Проверяем правильный URL перенаправления
    }

    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(post("/posts/{post_id}/comments/{comment_id}", postId.toString(), commentId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId.toString()));
    }
}

