package com.blogapp.dto;

public record CreatePostRequest(
        String title,
        String excerpt,
        String content,
        String tagsInput
) {
}
