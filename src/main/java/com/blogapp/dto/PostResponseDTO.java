package com.blogapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDTO(
        Long id,
        String title,
        String excerpt,
        String content,
        String author,
        LocalDateTime publishedAt,
        List<String> tags
) {
}