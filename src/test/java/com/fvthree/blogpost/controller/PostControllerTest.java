package com.fvthree.blogpost.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fvthree.blogpost.dto.CreateBlogPost;
import com.fvthree.blogpost.post.controller.PostController;
import com.fvthree.blogpost.post.entity.Post;
import com.fvthree.blogpost.post.repository.PostRepository;
import com.fvthree.blogpost.post.service.PostService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
	
	@Autowired
	private PostController controller;
	
	@MockBean
	private PostRepository postRepository;
	
	@MockBean
	private PostService postService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper = new ObjectMapper()
			.findAndRegisterModules()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@Test
	@WithMockUser(
			username = "fvthree",
			password = "testpassword",
			roles = {"DEV"})
	public void createPostTest() throws Exception {
		Post post = getMockPost();
		String jsonPost = objectMapper.writeValueAsString(post);
		
		when(postRepository.save(any(Post.class))).thenReturn(post);
		when(postRepository.existsByTitle(any())).thenReturn(false);
		when(postService.create(any(CreateBlogPost.class))).thenReturn(post);
		
		this.mockMvc.perform(post("/api/post")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPost))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(post.getId()))
				.andExpect(jsonPath("$.title").value(post.getTitle()))
				.andExpect(jsonPath("$.contentOne").value(post.getContentOne()))
				.andExpect(jsonPath("$.contentTwo").value(post.getContentTwo()))
				.andExpect(jsonPath("$.category").value(post.getCategory()))
				.andExpect(jsonPath("$.author").value(post.getAuthor()))
				.andExpect(jsonPath("$.image").value(post.getImage()));

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
	
	private CreateBlogPost updateBlogPost() {
		return CreateBlogPost.builder()
				.title("Lorem Ipsum2")
				.category("Personal")
				.author("Me")
				.image("Lorem.png")
				.contentOne("Lorem Ipsum dolor sit")
				.contentTwo("Lorem Ipsum dolor sit")
				.build();
	}
	
}
