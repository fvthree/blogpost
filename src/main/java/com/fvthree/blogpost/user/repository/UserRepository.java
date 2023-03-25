package com.fvthree.blogpost.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fvthree.blogpost.user.entity.User;

@Transactional
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);
	Page<User> findAll(Pageable pageable);
}
