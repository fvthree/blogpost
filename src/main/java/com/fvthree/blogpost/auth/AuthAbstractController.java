package com.fvthree.blogpost.auth;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.fvthree.blogpost.exceptions.HTTP400Exception;
import com.fvthree.blogpost.exceptions.HTTP404Exception;
import com.fvthree.blogpost.exceptions.RestAPIExceptionInfo;

import org.springframework.security.core.AuthenticationException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.micrometer.core.instrument.Counter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AuthAbstractController implements ApplicationEventPublisherAware {

	protected ApplicationEventPublisher eventPublisher;
	
	protected static final String DEFAULT_PAGE_SIZE = "20";
	protected static final String DEFAULT_PAGE_NUMBER= "0";
	
    @Autowired
    private Counter AuthHttp400ExceptionCounter;

    @Autowired
    private Counter AuthHttp404ExceptionCounter;
    
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AuthenticationException.class)
    public @ResponseBody RestAPIExceptionInfo handleAuthenticationException(AuthenticationException ex,
    		WebRequest request, HttpServletResponse response) {
    	log.info("Received Bad Request Exception " + ex.getLocalizedMessage());
    	AuthHttp400ExceptionCounter.increment();
    	return new RestAPIExceptionInfo("Invalid credentials.", "The request did not have correct parameters.");
    }
    
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({IllegalArgumentException.class, ExpiredJwtException.class, MalformedJwtException.class})
    public @ResponseBody RestAPIExceptionInfo handleJwtException(WebRequest request, HttpServletResponse response) {
    	AuthHttp400ExceptionCounter.increment();
    	return new RestAPIExceptionInfo("Authorization Header is missing, invalid or expired.", "The request did not have correct parameters.");
    }
    
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HTTP400Exception.class)
    public @ResponseBody RestAPIExceptionInfo handleBadRequestException(HTTP400Exception ex,
    		WebRequest request, HttpServletResponse response) {
    	log.info("Received Bad Request Exception " + ex.getLocalizedMessage());
    	AuthHttp400ExceptionCounter.increment();
    	return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The request did not have correct parameters.");
    }
    
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HTTP404Exception.class)
    public @ResponseBody RestAPIExceptionInfo handleResourceNotFoundException(HTTP404Exception ex,
    		WebRequest request, HttpServletResponse response) {
    	log.info("Received Resource Not Found Exception" + ex.getLocalizedMessage());
    	AuthHttp404ExceptionCounter.increment();
    	return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The requested resource was not found.");
    }
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
    	this.eventPublisher = eventPublisher;
    }
    
    public static <T> T checkResourceFound(final T resource) {
    	if (resource == null) {
    		throw new HTTP404Exception("Resource not found");
    	}
    	return resource;
    }
 }
