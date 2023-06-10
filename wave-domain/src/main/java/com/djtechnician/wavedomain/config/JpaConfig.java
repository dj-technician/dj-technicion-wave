package com.djtechnician.wavedomain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.djtechnician.wavedomain.repository")
public class JpaConfig {}
