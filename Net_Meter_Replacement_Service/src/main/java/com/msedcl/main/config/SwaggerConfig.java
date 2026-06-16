package com.msedcl.main.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "ApiKeyAuth";

        return new OpenAPI()

                .info(new Info()
                        .title("Maitri Solar API")
                        .version("1.0")
                        .description("MSEDCL Solar APIs"))

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )

                .components(
                        new Components()

                                .addSecuritySchemes(
                                        securitySchemeName,

                                        new SecurityScheme()

                                                .name("x-api-key")

                                                .type(SecurityScheme.Type.APIKEY)

                                                .in(SecurityScheme.In.HEADER)
                                )
                );
    }
}