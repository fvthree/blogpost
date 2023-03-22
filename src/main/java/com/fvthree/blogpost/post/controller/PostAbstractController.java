package com.fvthree.blogpost.post.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.fvthree.blogpost.exceptions.HTTP400Exception;
import com.fvthree.blogpost.exceptions.HTTP404Exception;
import com.fvthree.blogpost.exceptions.RestAPIExceptionInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PostAbstractController {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HTTP400Exception.class)
    public @ResponseBody RestAPIExceptionInfo handleBadRequestException(HTTP400Exception ex,
    		WebRequest request, HttpServletResponse response) {
    	log.info("Received Bad Request Exception " + ex.getLocalizedMessage());
    	return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The request did not have correct parameters.");
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HTTP404Exception.class)
    public @ResponseBody RestAPIExceptionInfo handleResourceNotFoundException(HTTP404Exception ex,
    		WebRequest request, HttpServletResponse response) {
    	log.info("Received Resource Not Found Exception" + ex.getLocalizedMessage());
    	return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The requested resource was not found.");
    }
}
