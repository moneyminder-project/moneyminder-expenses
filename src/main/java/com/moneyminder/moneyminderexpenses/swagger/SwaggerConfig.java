package com.moneyminder.moneyminderexpenses.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "API Gastos e Ingresos",
                description = "App que provee los diferentes métodos para acceder y persistir registros, detalles y presupuesto",
                version = "1.0.0",
                contact = @Contact(email = "smartinezpad@uoc.edu")
        ),
        servers = {
                @Server(
                        description = "dev server",
                        url = "http://localhost:18081"
                )
        },
        security = @SecurityRequirement(
                name = "Security token"
        )
)
@SecurityScheme(
        name = "Security token",
        description = "Se necesita acceso por token para acceder a la aplicación",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
