package io.github.tasoula.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "src/test/java/io/github/tasoula")
@PropertySource("classpath:test-application.properties")
public class WebTestConfiguration {}

