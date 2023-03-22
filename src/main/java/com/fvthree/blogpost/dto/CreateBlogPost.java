package com.fvthree.blogpost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBlogPost {
	
	private String image;
	
	private String title;
	
	private String contentOne;
	
	private String contentTwo;
	
	private String category;
	
	private String author;
}
