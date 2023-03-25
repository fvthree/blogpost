package com.fvthree.blogpost.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fvthree.blogpost.auth.jwt.AuthEntryPointJwt;
import com.fvthree.blogpost.auth.jwt.AuthTokenFilter;
import com.fvthree.blogpost.auth.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled=true)
public class WebSecurityConfig {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter()
    {
        return new AuthTokenFilter();
    }

    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
        throws Exception
    {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/refresh").authenticated()
            .antMatchers("/auth/check").authenticated()
            .antMatchers("/loggedout").permitAll()
            .antMatchers("/register").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/home").permitAll()
            .antMatchers("/users/resetPassword/{email}").permitAll()
            .antMatchers("/docs", // Swagger docs endpoints
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/webjars/**").permitAll()
            .antMatchers("/h2/console/**").permitAll() // H2 database calls during testing
            .anyRequest().authenticated()
            .and()
            .httpBasic();

        http.addFilterBefore(authenticationJwtTokenFilter(),
            UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
