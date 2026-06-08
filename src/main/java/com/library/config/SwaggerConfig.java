package com.library.config;

import com.google.common.collect.Lists;    // Guava — fine but discouraged for new code
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * Swagger2 Configuration using Springfox
 *
 * COMPLETELY BROKEN in Spring Boot 2.6+ and Spring Boot 3.x:
 * - springfox-swagger2 is ABANDONED — last release was 2020
 * - Spring Boot 2.6 changed default path matching to PathPatternParser, breaking Springfox
 * - Spring Boot 3 / Spring 6 removed support entirely
 * - Must migrate to springdoc-openapi:
 *   → Remove springfox-swagger2 + springfox-swagger-ui
 *   → Add springdoc-openapi-starter-webmvc-ui (for Boot 3) or springdoc-openapi-ui (for Boot 2.x)
 *   → Remove this entire class
 *   → Replace @EnableSwagger2 in main class
 *   → Swagger UI moves from /swagger-ui.html → /swagger-ui/index.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.library.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securityContexts(Lists.newArrayList(securityContext()))   // Guava Lists
                .securitySchemes(Lists.newArrayList(apiKey()));             // Guava Lists
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Library Management API")
                .description("REST API for Library Management System")
                .version("1.0.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
    }
}
