package com.fudaliarthur.webservices.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("CRUD Spring Boot JPA - Sistema de Pedidos")
                        .version("1.0")
                        .description("API RESTful para gerenciamento completo de pedidos, usuários, produtos e pagamentos."));
    }
}