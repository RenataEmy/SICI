package br.ifrn.edu.sici.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "SICI API",
        version = "1.0.0",
        description = "API do Sistema Integrado de Chamados Institucionais. Os identificadores dos usuários envolvidos nas operações são enviados no corpo da requisição."
))
public class OpenApiConfig {
    static {
        SpringDocUtils.getConfig().replaceParameterObjectWithClass(Pageable.class, org.springdoc.core.converters.models.Pageable.class);
    }
}
