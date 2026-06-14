package com.blogapp.repository;

import com.blogapp.entities.Post;
import com.blogapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Post> findByTagsId(Long tagId, Pageable pageable);

    @Query("SELECT DISTINCT p.author FROM Post p WHERE p.author IS NOT NULL")

    List<User> findDistinctAuthors();

    Page<Post> findByAuthor_NameIgnoreCase(String author, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseAndTagsId(String keyword, Long tagId, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseAndAuthor_NameIgnoreCase(String keyword, String author, Pageable pageable);

    Page<Post> findByTagsIdAndAuthor_NameIgnoreCase(Long tagId, String author, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseAndTagsIdAndAuthor_NameIgnoreCase(String keyword, Long tagId, String author, Pageable pageable);
}
