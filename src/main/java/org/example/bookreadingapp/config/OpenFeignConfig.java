package org.example.bookreadingapp.config;

import org.example.bookreadingapp.client.AuthorApiClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = AuthorApiClient.class)
@Configuration
public class OpenFeignConfig {


}
