package com.fvthree.blogpost.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("blogpost.app")
@Data
public class CustomPropertiesConfig {

	private String jwtSecret;
	
	private int jwtExpirationMs;
	
	private int jwtRefreshExpirationMs;
	
	private String memcachedActiveHost;
	
	private String memcachedActivePort;
	
	private String memcachedStandByHost;
	
	private String memcachedStandByPort;
}
