package com.blogapp.controller.api;

import com.blogapp.dto.CreatePostRequest;
import com.blogapp.dto.PostResponseDTO;
import com.blogapp.entities.Post;
import com.blogapp.repository.PostRepository;
import com.blogapp.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;
    private final PostRepository postRepository;
    @GetMapping
    public Page<PostResponseDTO> getPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "publishedAt") String sortField,
            @RequestParam(defaultValue = "desc") String order
    ) {

        Page<Post> posts = postService.getFilteredPosts(keyword, tagId, author, pageNo, sortField, order);

        return posts.map(postService::convertToDto);
    }

    @GetMapping("/{id}")
    public PostResponseDTO getPost(@PathVariable Long id){
        Post post = postService.getPostById(id);
        return postService.convertToDto(post);
    }

    @PostMapping
    public String createPost(@RequestBody CreatePostRequest request){
        Post post = new Post();
        post.setTitle(request.title());
        post.setExcerpt(request.excerpt());
        post.setContent(request.content());
        post.setTagsInput(request.tagsInput());
        postService.createPost(post, "kumar@abc.com");

        return "Post Created Successfully";
    }

    @PutMapping("/{id}")
    public String updatePost(@PathVariable Long id, @RequestBody CreatePostRequest request){

        Post post = new Post();
        post.setTitle(request.title());
        post.setExcerpt(request.excerpt());
        post.setContent(request.content());
        post.setTagsInput(request.tagsInput());
        postService.updatePost(id, post);
        return "Post Updated Successfully";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id){
        postService.deletePostById(id);
        return "Post Deleted Successfully";
    }
}