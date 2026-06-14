package com.blogapp.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private String title;
    private String excerpt;
    private String content;
    private String author;
    private String tags;
}
