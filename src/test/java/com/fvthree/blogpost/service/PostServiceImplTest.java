package com.fvthree.blogpost.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fvthree.blogpost.dto.CreateBlogPost;
import com.fvthree.blogpost.exceptions.HTTP404Exception;
import com.fvthree.blogpost.post.entity.Post;
import com.fvthree.blogpost.post.repository.PostRepository;
import com.fvthree.blogpost.post.service.PostService;
import com.fvthree.blogpost.post.service.PostServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
public class PostServiceImplTest {
	
	@Mock
	private PostRepository postRepository;
	
	@InjectMocks
	PostService postService = new PostServiceImpl();
	
	@DisplayName("Create Post - Success Scenario")
	@Test
	void test_when_Create_Post_Success() {
		// Mock
		Post post = getMockPost();
		// When
		when(postRepository.save(any(Post.class))).thenReturn(post);
		
		Post posted = postService.create(createBlogPost());
		
		// Then
		assertEquals(posted.getTitle(), post.getTitle());
	}
	
	@DisplayName("Get Post - Post Not Found Scenario")
	@Test
	void test_when_Get_Post_NOT_FOUND_then_Not_Found() {
		when(postRepository.findById(anyLong()))
			.thenReturn(Optional.ofNullable(null));
		
		HTTP404Exception exception =
					assertThrows(HTTP404Exception.class, 
							() -> postService.getPost(1L));
		assertEquals("Post not found", exception.getMessage());
	}
	
	@DisplayName("Get Post - Post Not Found Scenario")
	@Test
	void test_when_Get_Post_Success() {
		// Mock
		Post post = getMockPost();
		
		// When
		when(postRepository.findById(anyLong()))
			.thenReturn(Optional.of(post));
		
		// Actual
		Post posted = postService.getPost(1L);
		
		// Then
		assertNotNull(posted);
		assertEquals(posted.getTitle(), post.getTitle());
		assertEquals(posted.getAuthor(), post.getAuthor());
		assertEquals(posted.getCategory(), post.getCategory());
		assertEquals(posted.getImage(), post.getImage());
		assertEquals(posted.getContentOne(), post.getContentOne());
		assertEquals(posted.getContentTwo(), post.getContentTwo());

	}
	
	private Post getMockPost() {
		return Post.builder()
				.id(1L)
				.title("Lorem Ipsum")
				.category("Personal")
				.author("Me")
				.image("Lorem.png")
				.contentOne("Lorem Ipsum dolor sit")
				.contentTwo("Lorem Ipsum dolor sit")
				.dateCreated(LocalDateTime.now())
				.lastUpdated(LocalDateTime.now())
				.build();
	}
	
	private CreateBlogPost createBlogPost() {
		return CreateBlogPost.builder()
				.title("Lorem Ipsum")
				.category("Personal")
				.author("Me")
				.image("Lorem.png")
				.contentOne("Lorem Ipsum dolor sit")
				.contentTwo("Lorem Ipsum dolor sit")
				.build();
	}
}
