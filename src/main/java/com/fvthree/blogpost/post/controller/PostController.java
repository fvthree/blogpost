package com.fvthree.blogpost.post.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fvthree.blogpost.dto.CreateBlogPost;
import com.fvthree.blogpost.post.entity.Post;
import com.fvthree.blogpost.post.service.PostService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class PostController extends PostAbstractController {
	
	@Autowired
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@PostMapping("/post")
	public ResponseEntity<?> createPost(@Valid @RequestBody CreateBlogPost request) {
		log.info(request.getContentOne());
		Post post = postService.create(request);
		return ResponseEntity.ok().body(post);
	}
	
	@PutMapping("/post/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody CreateBlogPost request , @PathVariable(name="id") Long id) {
		return new ResponseEntity<>(postService.update(id, request), HttpStatus.OK);
	}
	
	@DeleteMapping("/post/{id}")
	public ResponseEntity<?> update(@PathVariable(name="id") Long id) {
		postService.remove(id);
		return new ResponseEntity<>("Post deleted successfully.", HttpStatus.OK);
	}
	
	@GetMapping("/post/{id}")
	public ResponseEntity<?> getPostById(@PathVariable(name="id") Long id) {
		return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
	}
	
	@GetMapping("/post/category/{category}")
	public ResponseEntity<?> getPostsByCategory(@PathVariable(name="category") String category) {
		return new ResponseEntity<>(postService.getPostByCategory(category), HttpStatus.OK);
	}
	
}
