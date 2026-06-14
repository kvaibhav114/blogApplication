package com.blogapp.service;

import com.blogapp.dto.CommentDTO;
import com.blogapp.entities.Comment;
import com.blogapp.entities.Post;
import com.blogapp.repository.CommentRepository;
import com.blogapp.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public CommentService(CommentRepository commentRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public void createComment(Long postId, Comment comment){
        Post post = postRepository.findById(postId).orElseThrow();
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsPerPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findByPost(post);
    }

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

    public Comment getCommentById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow();
    }

    public void updateComment(Long commentId, String updatedComment){
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setComment(updatedComment);
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public CommentDTO convertToDto(Comment comment){
        return new CommentDTO(comment.getName(), comment.getEmail(), comment.getComment());
    }
}
