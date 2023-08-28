package com.example.AppWinterhold.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI appWinterholdOpenApi(){
        String schemaName = "bearerAuth";
        SecurityRequirement requirement = new SecurityRequirement().addList(schemaName);

//        SecurityScheme scheme = new SecurityScheme().name(schemaName)
//                .name(schemaName)
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT");
//
//        Components component = new Components().addSecuritySchemes(schemaName,scheme);

        Info info = new Info().title("App WinterHold Open API")
                .version("V 1.0.0")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        OpenAPI openAPI = new OpenAPI().addSecurityItem(requirement)
//                .components(component)
                .info(info);
        return openAPI;
    }
}
