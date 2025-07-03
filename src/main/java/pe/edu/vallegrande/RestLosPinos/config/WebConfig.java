package pe.edu.vallegrande.RestLosPinos.config;

import org.springframework.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:8083",
                    "http://localhost:8080",
                    "http://10.0.2.2",
                    "http://localhost:4200",
                    "https://8083-firebase-as232s4t02-be-1748901890569.cluster-4xpux6pqdzhrktbhjf2cumyqtg.cloudworkstations.dev",
                    "http://localhost"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
