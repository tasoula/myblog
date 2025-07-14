package io.github.tasoula.blog_app.controller;

import io.github.tasoula.blog_app.dto.Comment;
import io.github.tasoula.blog_app.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/posts/{id}/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping()
    public String addComment(@PathVariable("id") UUID id, @ModelAttribute("comment") Comment comment) {
        service.create(id, comment);
        return "redirect:/posts/" + id;
    }

    @PostMapping("{comment_id}")
    public String editComment(@PathVariable("id") UUID id,
                              @ModelAttribute("comment") Comment comment) {
        service.update(comment);
        return "redirect:/posts/" + id;
    }

    @PostMapping("{comment_id}/delete")
    public String deleteComment(@PathVariable("id") UUID id,
                                @PathVariable("comment_id") UUID commentId) {
        service.delete(commentId);
        return "redirect:/posts/" + id;
    }
}
