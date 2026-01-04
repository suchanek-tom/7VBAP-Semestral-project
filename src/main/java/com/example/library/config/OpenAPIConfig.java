package com.example.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for the Library Management System API.
 * This configuration defines the API documentation that will be available at:
 * - Swagger UI: http://localhost:8080/swagger-ui/index.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 * - OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Configures the OpenAPI documentation with API metadata and JWT security scheme.
     * 
     * @return OpenAPI configuration object
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Library Management System API")
                        .description("REST API for managing books, loans, users, and authors in a library system. " +
                                   "This API provides endpoints for CRUD operations on library resources with " +
                                   "role-based access control (Admin and User roles). " +
                                   "\n\n**Authentication:** Most endpoints require JWT authentication. " +
                                   "Use the `/api/users/login` endpoint to obtain a JWT token, then click the " +
                                   "'Authorize' button and enter the token in the format: `Bearer <your-token>`")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("OPR3 Library Team")
                                .email("support@library.example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from /api/users/login endpoint")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}
