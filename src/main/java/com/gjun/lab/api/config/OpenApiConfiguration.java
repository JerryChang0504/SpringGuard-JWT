package com.gjun.lab.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfiguration {

	@Bean
	public OpenAPI mvcDemoOpenAPI() {
		final String bearer = "bearer-jwt";
		return new OpenAPI()
				.info(new Info().title("mvc-demo API").description("JWT 與 Spring Security Lab — REST 端點說明").version("0.0.1"))
				.addSecurityItem(new SecurityRequirement().addList(bearer))
				.components(new Components().addSecuritySchemes(bearer,
						new SecurityScheme()
								.name(bearer)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
								.description("先呼叫 POST /api/auth/login 取得 accessToken，再點選 Authorize 填入")));
	}
}
