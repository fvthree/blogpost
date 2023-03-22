package com.fvthree.blogpost.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fvthree.blogpost.auth.entity.ERole;
import com.fvthree.blogpost.auth.entity.Role;

@Transactional
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByName(ERole name);
}
