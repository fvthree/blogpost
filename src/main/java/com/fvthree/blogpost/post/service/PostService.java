package com.fvthree.blogpost.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fvthree.blogpost.dto.CreateBlogPost;
import com.fvthree.blogpost.post.entity.Post;

@Service
@Transactional
public interface PostService {
	Post create(CreateBlogPost req);
	Post update(Long id, CreateBlogPost req);
	void remove(Long id);
	Post getPost(Long id);
	List<Post> getPostByCategory(String category);
}
