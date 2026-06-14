package com.blogapp.controller.api;

import com.blogapp.dto.CommentDTO;
import com.blogapp.entities.Comment;
import com.blogapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class CommentRestController {
    private final CommentService commentService;

    @GetMapping("/{postId}/comments")
    public List<CommentDTO> getComments(@PathVariable Long postId){
        return commentService.getCommentsPerPost(postId).stream().map(commentService::convertToDto).toList();
    }

    @PostMapping("/{postId}/comments")
    public String createComment(@PathVariable Long postId, @RequestBody CommentDTO request){
        Comment comment = new Comment();
        comment.setName(request.getName());
        comment.setEmail(request.getEmail());
        comment.setComment(request.getComment());
        commentService.createComment(postId, comment);
        return "Comment Created Successfully";
    }

    @PutMapping("/comments/{commentId}")
    public String updateComment(@PathVariable Long commentId, @RequestBody String comment){
        commentService.updateComment(commentId, comment);
        return "Comment Updated Successfully";
    }

    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return "Comment Deleted Successfully";
    }
}
