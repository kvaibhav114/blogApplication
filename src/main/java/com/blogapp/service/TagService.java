package com.blogapp.service;

import com.blogapp.entities.Tag;
import com.blogapp.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    public TagService(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }
    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }
}
