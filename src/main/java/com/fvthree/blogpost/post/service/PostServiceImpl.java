package com.fvthree.blogpost.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fvthree.blogpost.dto.CreateBlogPost;
import com.fvthree.blogpost.exceptions.HTTP400Exception;
import com.fvthree.blogpost.exceptions.HTTP404Exception;
import com.fvthree.blogpost.post.entity.Post;
import com.fvthree.blogpost.post.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PostServiceImpl implements PostService {
	
	@Autowired
	private final PostRepository postRepository;
	
	public PostServiceImpl(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Override
	public Post create(CreateBlogPost req) {
		
		if (postRepository.existsByTitle(req.getTitle())) {
			throw new HTTP400Exception("title already exists.");
		}
		
		log.info(req.getContentOne());
		
		Post post = Post.builder()
				.title(req.getTitle())
				.contentOne(req.getContentOne())
				.contentTwo(req.getContentTwo())
				.author(req.getAuthor())
				.category(req.getCategory())
				.image(req.getImage())
				.build();
		
		log.info(post.getContentOne());
		
		return postRepository.save(post);
	}

	@Override
	public Post update(Long id, CreateBlogPost req) {
		Post postInDB = postRepository.findById(id)
				.orElseThrow(() -> new HTTP404Exception("Post not found."));
		
		// check title unique constraint
		if (!req.getTitle().equals(postInDB.getTitle())) {
			if (postRepository.existsByTitle(req.getTitle())) {
				throw new HTTP400Exception("title already exists.");
			}
		}
		
		postInDB.setTitle(req.getTitle());
		postInDB.setContentOne(req.getContentOne());
		postInDB.setContentTwo(req.getContentTwo());
		postInDB.setImage(req.getImage());
		postInDB.setCategory(req.getCategory());
		postInDB.setAuthor(req.getAuthor());
		
		return postRepository.save(postInDB);
	}

	@Override
	public void remove(Long id) {
		postRepository.deleteById(id);
	}

	@Override
	public Post getPost(Long id) {
		return postRepository.findById(id)
				.orElseThrow(() -> new HTTP404Exception("Post not found"));
	}

	@Override
	public List<Post> getPostByCategory(String category) {
		return postRepository.findByCategory(category);
	}

}
