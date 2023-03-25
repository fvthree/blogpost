package com.fvthree.blogpost.post.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="post",
		uniqueConstraints = {
				@UniqueConstraint(columnNames="title")
		})
public class Post implements Serializable {

	private static final long serialVersionUID = 584510760798749663L;
	
	@Id
	@SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "post_sequence")
	@Column(name="post_id")
	private Long id;
	
	@Column(name="title", nullable=false)
	private String title;
	
	@Column(name="content_one", nullable=false)
	private String contentOne;
	
	@Column(name="content_two", nullable=false)
	private String contentTwo;
	
	@Column(name="image")
	private String image;
	
	@Column(name="category", nullable=false)
	private String category;
	
	@Column(name="author", nullable=false)
	private String author;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(updatable=false, name="date_created")
	private LocalDateTime dateCreated;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(name="last_updated")
	private LocalDateTime lastUpdated;
	
	@PrePersist
	public void setDateCreated() {
		this.dateCreated = LocalDateTime.now();
	}
	
	@PreUpdate
	public void setLastUpdated() {
		this.lastUpdated = LocalDateTime.now();
	}
}
