package com.fvthree.blogpost.post.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fvthree.blogpost.post.entity.Post;

@Transactional
@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
	boolean existsByTitle(String title);
	List<Post> findByCategory(String category);
}
