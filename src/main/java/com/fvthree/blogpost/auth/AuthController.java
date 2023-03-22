package com.fvthree.blogpost.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fvthree.blogpost.auth.dto.AuthCheckStatus;
import com.fvthree.blogpost.auth.dto.JwtResponse;
import com.fvthree.blogpost.auth.dto.LoginRequest;
import com.fvthree.blogpost.auth.dto.SignupRequest;
import com.fvthree.blogpost.auth.entity.ERole;
import com.fvthree.blogpost.auth.entity.Role;
import com.fvthree.blogpost.auth.jwt.JwtUtils;
import com.fvthree.blogpost.auth.repository.RoleRepository;
import com.fvthree.blogpost.auth.services.AuthService;
import com.fvthree.blogpost.auth.services.UserDetailsImpl;
import com.fvthree.blogpost.auth.services.UserDetailsServiceImpl;
import com.fvthree.blogpost.exceptions.HTTP400Exception;
import com.fvthree.blogpost.user.entity.User;
import com.fvthree.blogpost.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class AuthController extends AuthAbstractController {

    private static final String ROLE_NOT_FOUND_ERROR_MESSAGE = "Error: Role is not found.";
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private AuthService authUserService;
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(
        @Valid @RequestBody LoginRequest loginRequest)
    {
        return ResponseEntity.ok(authUserService
            .authenticateUser(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(
        @Valid @RequestBody SignupRequest signUpRequest,
        @RequestParam(required = false, defaultValue = "false") boolean sendPasswordEmail) {
        if (userRepository.existsByName(signUpRequest.getName())) {
        	throw new HTTP400Exception("Name already exists.");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
        	throw new HTTP400Exception("Email already exists.");
        }

        // Create new user's account
        User user = User.builder()
        		.email(signUpRequest.getEmail())
				.password(encoder.encode(signUpRequest.getPassword()))
				.build();

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR_MESSAGE));
        
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(authUserService
            .authenticateUser(signUpRequest.getEmail(), signUpRequest.getPassword()));
    }

    /**
     * Refreshes the user's pair of tokens by generating a new set of
     * <code>accessToken</code> and
     * <code>refreshToken</code>, and invalidating the old pair.
     *
     * @param request an HTTP request.
     * @return a <code>JwtResponse</code> containing the new user information.
     */
    @GetMapping("/refresh")
    public JwtResponse refreshToken(
        HttpServletRequest request)
    {
        try
        {
            // Extract refresh token from request.
            String token = jwtUtils.parseJwt(request);
            String username = jwtUtils.getUserNameFromJwtToken(token);

            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                .loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

            // Remove the old pair of tokens from the whitelist.
            jwtUtils.removeWhitelistJwtPair(token);

            // Generate a new set of tokens for the user.
            String accessToken = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateJwtRefreshToken(authentication);

            // Whitelist the new pair of tokens.
            jwtUtils.whitelistJwtPair(accessToken, refreshToken);

            return new JwtResponse(accessToken,
                refreshToken,
                userDetails.getEmail(),
                roles.get(0));
        } catch (Exception e)
        {
            log.error("Failed to refresh the authentication token.", e);
            throw new HTTP400Exception("Failed to refresh the authentication token. Try again later.", e);
        }
    }

    /**
     * Logs out the user by taking the token from the <code>Authorization</code>
     * header,
     * extracting the username from its body, and invalidating all of the tokens
     * that
     * belong to that user.
     *
     * @param request an HTTP request.
     * @return a <code>Response Entity</code> with no body.
     */
    @GetMapping("/loggedout")
    public ResponseEntity<?> logout(HttpServletRequest request)
    {
        try
        {
            // Extract refresh token from request.
            String token = jwtUtils.parseJwt(request);
            String user = jwtUtils.getUserNameFromJwtToken(token);
            
        	log.info(user);

            // Remove all User's tokens from the whitelist
            jwtUtils.removeWhitelistUser(user);
            
            return ResponseEntity.ok("User logged out.");
        } catch (Exception e)
        {
            log.error("Failed to logout.", e);
            throw e;
        }
    }
    
    @GetMapping("/auth/check")
    public ResponseEntity<?> authCheck(HttpServletRequest request) {
    	log.info("request header auth ::: " + request.getHeader("Authorization"));
    	return ResponseEntity.ok(new AuthCheckStatus("ok"));
    }
}
