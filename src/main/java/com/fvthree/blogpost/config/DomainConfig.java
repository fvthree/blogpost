package com.fvthree.blogpost.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("com.fvthree.blogpost")
@EnableJpaRepositories("com.fvthree.blogpost")
@EnableTransactionManagement
public class DomainConfig {
}
