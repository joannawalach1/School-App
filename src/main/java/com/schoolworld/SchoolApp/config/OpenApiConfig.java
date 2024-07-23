package com.schoolworld.SchoolApp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "School Management System API",
                version = "1.0",
                description = "API for managing school operations",
                contact = @Contact(name = "Jola Walach", email = "jola.walach@example.com"),
                license = @License(name = "Apache 2.0", url = "http://springdoc.org")
        )
)
public class OpenApiConfig {
}
