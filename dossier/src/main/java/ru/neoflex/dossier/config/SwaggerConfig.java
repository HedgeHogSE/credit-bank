package ru.neoflex.dossier.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI dossierOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dossier Service API")
                        .description("Dossier service consumes Kafka events and sends notification emails. "
                                + "Main event topics: finish-registration, create-documents, statement-denied, "
                                + "send-documents, send-ses, credit-issued.")
                        .version("1.0.0")
                        .contact(new Contact().name("Credit Bank Team")));
    }
}
