package com.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.example.app.annotation.MethodMetadata;

@Configuration
public class AppConfig {

    @Bean
    @MethodMetadata(irId = "custom:config:AppConfig:restTemplate()", hash = "681ea929", zone = 1)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
