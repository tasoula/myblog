package io.github.tasoula.blog_app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID postId;
    private  UUID commentId;

    @BeforeEach
    void setUp() {
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

