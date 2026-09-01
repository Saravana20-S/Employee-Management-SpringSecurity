package com.bridgelabz.employeemanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeManagementOpenAPI() {

        return new OpenAPI()

                .info(
                        new Info()

                                .title(
                                        "Employee Data Management API"
                                )

                                .version("1.0.0")

                                .description(
                                        """
                                        Employee Data Management &
                                        Batch Processing System

                                        Features:
                                        - OAuth2 Login
                                        - Excel Upload
                                        - RabbitMQ
                                        - Spring Batch
                                        - PostgreSQL
                                        - AOP Audit Logging
                                        """
                                )

                                .contact(
                                        new Contact()
                                                .name(
                                                        "BridgeLabz"
                                                )
                                )

                                .license(
                                        new License()
                                                .name(
                                                        "API License"
                                                )
                                )
                );
    }
}