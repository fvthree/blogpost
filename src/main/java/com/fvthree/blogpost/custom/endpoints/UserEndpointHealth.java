package com.fvthree.blogpost.custom.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Endpoint(id = "is-app-up")
@Slf4j
public class UserEndpointHealth {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@ReadOperation
	public String IsCustomerHealth() {
		String uri = "http://localhost:8080/home";
		
		try {
			String result = restTemplate.getForObject(uri, String.class);
			log.info(result);
			return "SUCCESS";
		} catch (Exception e) {
			log.error("Health endpoint failing with : " + e.getMessage());
			return "FAILURE";
		}
	}
	
	@WriteOperation
    public void writeOperation(@Selector String name) {
		log.info("write operation");
	}
	
	@DeleteOperation
    public void deleteOperation(@Selector String name) {
		log.info("delete operation");
	}
}
