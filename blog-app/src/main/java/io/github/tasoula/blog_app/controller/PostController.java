package io.github.tasoula.blog_app.controller;

import io.github.tasoula.blog_app.service.ImageService;
import io.github.tasoula.blog_app.service.PostService;
import io.github.tasoula.blog_app.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("edit")
    public String edit(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("post", service.getPost(id));
        return "new-post";
    }
    @PostMapping()
    public String save(@PathVariable("id") UUID id,
                       @RequestParam("title") String title,
                       @RequestParam("image") MultipartFile image,
                       @RequestParam("tags") String tags,
                       @RequestParam("content") String content) {

        service.update(id, title, image, tags, content);
        return "redirect:/posts/{id}";
    }

    @PostMapping("delete")
    public String delete(@PathVariable("id") UUID id, Model model) {
        service.delete(id);
        return "redirect:/posts";
    }

    @PostMapping("like")
    public String like(@PathVariable("id") UUID id, @RequestParam("like") boolean like) {
        service.addLike(id, like);
        return "redirect:/posts/{id}";
    }
}
