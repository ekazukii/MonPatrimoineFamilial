package fr.cytech.mpf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Handle cors mapping for all controllers
 */
@Configuration
public class CorsWebMVC {
    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:5173", "http://127.0.0.1:5173", "https://localhost:3000", "https://mpftree.site")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true).maxAge(3600);
            }
        };
    }

}
