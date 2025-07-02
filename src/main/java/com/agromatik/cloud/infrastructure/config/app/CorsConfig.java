package com.agromatik.cloud.infrastructure.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todos los endpoints
                        .allowedOrigins("*") // Permite todos los orígenes (en producción, especifica los dominios)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                        .allowedHeaders("*") // Cabeceras permitidas
                        .allowCredentials(false) // Si necesitas cookies/autenticación, cambia a true
                        .maxAge(3600); // Tiempo de cache para las opciones pre-flight
            }
        };
    }
}