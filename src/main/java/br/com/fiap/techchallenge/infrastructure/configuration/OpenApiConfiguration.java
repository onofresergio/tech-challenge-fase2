package br.com.fiap.techchallenge.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tech Challenge API",
                version = "v2",
                description = "API para o Tech Challenge FIAP. Baseada na versão REST /api/v2.",
                contact = @Contact(
                        name = "Onofre Lima"
                )
        )
)
public class OpenApiConfiguration {
}
