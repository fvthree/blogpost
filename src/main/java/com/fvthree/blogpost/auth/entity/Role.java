package com.fvthree.blogpost.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

	@Id
	@SequenceGenerator(
            name = "roles_sequence",
            sequenceName = "roles_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_sequence")
    @Column(name = "role_id", updatable = false, nullable = false)
	private Long roleId;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole name;
	
	@CreationTimestamp
    @Column(name = "created_at", nullable = false)
	private Date createdAt;
	
	@UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
	private Date updatedAt;
}
