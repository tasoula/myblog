package io.github.tasoula.config;

import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataSourceTestConfiguration {

    // Настройка DataSource — компонент, отвечающий за соединение с базой данных
    @Bean
    public DataSource dataSource(
            // Настройки соединения возьмём из Environment
            @Value("${spring.datasource.test.url}") String url,
            @Value("${spring.datasource.test.username}") String username,
            @Value("${spring.datasource.test.password}") String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("posts.sql"));
        populator.addScript(new ClassPathResource("comments.sql"));
        populator.addScript(new ClassPathResource("tags.sql"));
        populator.execute(dataSource);

        return dataSource;
    }

    // JdbcTemplate — компонент для выполнения запросов
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}