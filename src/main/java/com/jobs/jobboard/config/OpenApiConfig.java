package com.jobs.jobboard.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobboardOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Jobboard API")
                        .description("Jobboard / LinkedIn-like backend API (users, companies, jobs, applications)")
                        .version("v1"));
    }
}

