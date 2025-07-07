package io.github.tasoula.controller;

import io.github.tasoula.service.ImageService;
import io.github.tasoula.service.PostService;
import io.github.tasoula.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/posts/{id}")
public class PostController {
    private final PostService service;

    public PostController(PostService service, ImageService imageService, TagService tagService) {
        this.service = service;
    }

    @GetMapping()
    public String show(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("post", service.getPost(id));
        return "post";
    }

}
