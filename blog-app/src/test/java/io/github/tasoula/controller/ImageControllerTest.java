package io.github.tasoula.controller;

import io.github.tasoula.config.DataSourceTestConfiguration;
import io.github.tasoula.config.WebTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceTestConfiguration.class, WebTestConfiguration.class})
class ImageControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID postId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.execute("DELETE FROM t_posts");

        postId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO t_posts (id, title, image_url) VALUES (?, ?, ?)",
                postId, "title", "12345_test_image.jpg");

    }

    @Test
    void image() throws Exception {
        mockMvc.perform(get("/posts/images/{id}", postId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
             //   .andExpect(view().name("post"))
             //   .andExpect(model().attributeExists("post"))
        ;
    }
}