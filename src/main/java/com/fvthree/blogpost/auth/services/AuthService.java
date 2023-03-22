package com.fvthree.blogpost.auth.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fvthree.blogpost.auth.dto.JwtResponse;
import com.fvthree.blogpost.auth.jwt.JwtUtils;
import com.fvthree.blogpost.exceptions.HTTP404Exception;
import com.fvthree.blogpost.user.entity.User;
import com.fvthree.blogpost.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
public class AuthService {
	
	@Autowired
	private final AuthenticationManager authenticationManager;
    
	@Autowired
	private final UserRepository userRepository;
    
	@Autowired
	private final PasswordEncoder encoder;
    
	@Autowired
	private final JwtUtils jwtUtils;
	
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
			PasswordEncoder encoder, JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
	}

	public JwtResponse authenticateUser(final String email, final String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String jwtRefresh = jwtUtils.generateJwtRefreshToken(authentication);

        jwtUtils.whitelistJwtPair(jwt, jwtRefresh);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        return new JwtResponse(jwt,
	            jwtRefresh,
	            userDetails.getEmail(),
	            roles.get(0));
    }

    public User getCurrentlyAuthenticatedUser() {
    	UserDetailsImpl userDetails =
            (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new HTTP404Exception("User not found"));
    }

    public String encodePassword(String password) {
        return encoder.encode(password);
    }
}
