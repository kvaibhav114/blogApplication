package com.blogapp.service;

import com.blogapp.dto.PostDTO;
import com.blogapp.dto.PostResponseDTO;
import com.blogapp.entities.Post;
import com.blogapp.entities.Tag;
import com.blogapp.entities.User;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.TagRepository;
import com.blogapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    public PostService(PostRepository postRepository, TagRepository tagRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }
    public void createPost(Post post, String email){
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findByEmail(email).orElseThrow();
        post.setAuthor(user);
        post.setCreatedAt(now);
        post.setPublishedAt(now);
        post.setUpdatedAt(now);
        post.setPublished(true);
        Set<Tag> postTags = new HashSet<>();
        String tagsInput = post.getTagsInput();
        String[] tags = tagsInput.split(",");
        for(String tag : tags){
            String temp = tag.toLowerCase().trim();
            Optional<Tag> exists = tagRepository.findByName(temp);
            if(exists.isPresent()){
                postTags.add(exists.get());
            }
            else {
               Tag newTag = new Tag();
               newTag.setName(temp);
               tagRepository.save(newTag);
               postTags.add(newTag);
            }
        }
        post.setTags(postTags);
        postRepository.save(post);
    }

    public Page<Post> getAllPosts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        return postRepository.findAll(pageable);
    }

    public Post getPostById(Long id){
        return postRepository.findById(id).orElseThrow();
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    public void updatePost(Long id, Post updatedPost) {

        Post existingPost = postRepository.findById(id).orElseThrow();

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setExcerpt(updatedPost.getExcerpt());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());
        postRepository.save(existingPost);
    }

    public Page<Post> searchPosts(String keyword, int pageNo){
        Pageable pageable = PageRequest.of(pageNo, 5);
        return postRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public Page<Post> getPostsByTag(Long tagId, int pageNo){
        Pageable pageable = PageRequest.of(pageNo, 5);
        return postRepository.findByTagsId(tagId, pageable);
    }

    public List<User> getAllAuthors(){
        return postRepository.findDistinctAuthors();
    }

    public Page<Post> getFilteredPosts(String keyword, Long tagId, String author, int pageNo, String sortField, String order) {

        if ("author".equals(sortField)) {
            sortField = "author.name";
        }

        Sort sort;

        if (order.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortField).ascending();
        } else {
            sort = Sort.by(sortField).descending();
        }

        Pageable pageable = PageRequest.of(pageNo, 5, sort);

        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasTag = tagId != null;
        boolean hasAuthor = author != null && !author.isBlank();

        if (hasKeyword && hasTag && hasAuthor) {
            return postRepository.findByTitleContainingIgnoreCaseAndTagsIdAndAuthor_NameIgnoreCase(keyword, tagId, author, pageable);
        }

        if (hasKeyword && hasTag) {
            return postRepository.findByTitleContainingIgnoreCaseAndTagsId(keyword, tagId, pageable);
        }

        if (hasKeyword && hasAuthor) {
            return postRepository.findByTitleContainingIgnoreCaseAndAuthor_NameIgnoreCase(keyword, author, pageable);
        }

        if (hasTag && hasAuthor) {
            return postRepository.findByTagsIdAndAuthor_NameIgnoreCase(tagId, author, pageable);
        }

        if (hasKeyword) {
            return postRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        }

        if (hasTag) {
            return postRepository.findByTagsId(tagId, pageable);
        }

        if (hasAuthor) {
            return postRepository.findByAuthor_NameIgnoreCase(author, pageable);
        }

        return postRepository.findAll(pageable);
    }

    public PostResponseDTO convertToDto(Post post){
        List<String> tags = post.getTags().stream().map(Tag::getName).toList();
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getExcerpt(), post.getContent(), post.getAuthor().getName(), post.getPublishedAt(), tags);
    }
}
