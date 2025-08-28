package mk.ukim.finki.synergymed.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        final String BEARER = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("SynergyMed API").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(BEARER,
                                new SecurityScheme()
                                        .name(BEARER)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}