package io.github.tasoula.controller;

import io.github.tasoula.dto.Post;
import io.github.tasoula.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/posts")
public class PostFeedController {
    private final PostService service;

    public PostFeedController(PostService service) {
        this.service = service;
    }

    @GetMapping()
    public String show(@RequestParam(name = "search", required = false) String tags,
                       @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                       Model model) {

        model.addAttribute("search", tags);

        if (pageSize <= 0) pageSize = 10;
        if (pageNumber < 0) pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").descending());

        Page<Post> postsPage = service.findByTags(tags, pageable);
        model.addAttribute("paging", postsPage);
        model.addAttribute("posts", postsPage.getContent());
        return "posts";
    }

    @GetMapping("new")
    public String newPost(Model model) {
        return "new-post";
    }

    @PostMapping()
    public String create(@RequestParam("title") String title,
                         @RequestParam("image") MultipartFile image,
                         @RequestParam("tags") String tags,
                         @RequestParam("content") String content
    ) {
        service.create(title, image, tags, content);
        return "redirect:/posts";
    }
}
