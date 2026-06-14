package com.blogapp.controller;

import com.blogapp.dto.CommentDTO;
import com.blogapp.entities.Comment;
import com.blogapp.entities.Post;
import com.blogapp.entities.Role;
import com.blogapp.entities.User;
import com.blogapp.repository.UserRepository;
import com.blogapp.service.CommentService;
import com.blogapp.service.PostService;
import com.blogapp.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService service;
    private final CommentService commentService;
    private final TagService tagService;
    private final UserRepository userRepository;
    public PostController(PostService service, CommentService commentService, TagService tagService, UserRepository userRepository){
        this.service = service;
        this.commentService = commentService;
        this.tagService = tagService;
        this.userRepository = userRepository;

    }


    @GetMapping("/new")
    public String showPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "post-form";
    }

    @PostMapping
    public String createPost(@ModelAttribute Post post, Authentication authentication) {
        String email = authentication.getName();
        service.createPost(post, email);
        return "redirect:/posts";
    }


    @GetMapping
    public String getAllPosts(
            Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "publishedAt") String sortField,
            @RequestParam(defaultValue = "desc") String order){
        Page<Post> page = service.getFilteredPosts(keyword, tagId, author, pageNo, sortField, order);

        model.addAttribute("posts", page.getContent());
        model.addAttribute("postPage", page);
        model.addAttribute("currentPage", pageNo);

        model.addAttribute("keyword", keyword);
        model.addAttribute("tagId", tagId);
        model.addAttribute("author", author);

        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("authors", service.getAllAuthors());

        model.addAttribute("sortField", sortField);
        model.addAttribute("order", order);
        return "posts";
    }

    @GetMapping("/{id}")
    public String getDetailedPost(Model model, @PathVariable Long id) {
        Post post = service.getPostById(id);
        List<Comment> comments = commentService.getCommentsPerPost(id);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", new Comment());
        return "post-detail";
    }


    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, Authentication authentication){
        Post post = service.getPostById(id);
        if(!canModifyPost(post, authentication.getName())){
            return "error";
        }
        service.deletePostById(id);

        return "redirect:/posts";
    }

    @GetMapping("/edit/{id}")
    public String editPostDetails(Model model, @PathVariable Long id, Authentication authentication){
        Post post = service.getPostById(id);
        if(!canModifyPost(post, authentication.getName())){
            return "error";
        }
        model.addAttribute("post", post);
        return "edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute Post post, Authentication authentication){
        Post existingPost = service.getPostById(id);
        if(!canModifyPost(existingPost, authentication.getName())){
            return "error";
        }
        service.updatePost(id, post);

        return "redirect:/posts";
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id, @RequestParam String name, @RequestParam String email, @RequestParam String comment){
        Comment c = new Comment();
        c.setName(name);
        c.setEmail(email);
        c.setComment(comment);
        commentService.createComment(id, c);

        return "redirect:/posts/" + id;
    }

    @PostMapping("/{postId}/comments/delete/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication){

        Comment comment = commentService.getCommentById(commentId);

        if(!canModifyComment(comment, authentication.getName())){
            return "error";
        }

        commentService.deleteComment(commentId);

        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{postId}/comments/edit/{commentId}")
    public String editComment(@PathVariable Long postId, @PathVariable Long commentId, Model model, Authentication authentication){
        Comment comment = commentService.getCommentById(commentId);
        if(!canModifyComment(comment, authentication.getName())){
            return "error";
        }

        model.addAttribute("comment", comment);
        model.addAttribute("postId", postId);

        return "edit-comment";
    }

    @PostMapping("/{postId}/comments/edit/{commentId}")
    public String updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam String comment, Authentication authentication){
        Comment existingComment = commentService.getCommentById(commentId);
        if(!canModifyComment(existingComment, authentication.getName())){
            return "error";
        }
        commentService.updateComment(commentId, comment);

        return "redirect:/posts/" + postId;
    }

    private boolean canModifyPost(Post post, String email){
        User currentUser = userRepository.findByEmail(email).orElseThrow();
        return currentUser.getRole() == Role.ADMIN || post.getAuthor().getId().equals(currentUser.getId());
    }

    private boolean canModifyComment(Comment comment, String email){
        User currentUser = userRepository.findByEmail(email).orElseThrow();
        return currentUser.getRole() == Role.ADMIN || comment.getPost().getAuthor().getId().equals(currentUser.getId());
    }
}