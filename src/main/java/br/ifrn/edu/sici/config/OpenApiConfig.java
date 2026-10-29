package br.ifrn.edu.sici.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "SICI API",
        version = "1.0.0",
        description = "API do Sistema Integrado de Chamados Institucionais. Os identificadores dos usuários envolvidos nas operações são enviados no corpo da requisição."
))
public class OpenApiConfig {
}
