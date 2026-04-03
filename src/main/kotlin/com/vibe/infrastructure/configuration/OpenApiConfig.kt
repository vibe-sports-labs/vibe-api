package com.vibe.infrastructure.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi // Importante!
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("vibe-v1")
            .pathsToMatch("/api/v1/**")
            .build()
    }

    @Bean
    fun vibeOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Vibe Sports Labs - API")
                    .description("Plataforma para organização de eventos esportivos.")
                    .version("1.0.0")
            )
    }
}