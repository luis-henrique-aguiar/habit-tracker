package com.habittracker.presentation.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI habitTrackerOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        developmentServer(),
                        productionServer()
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("Habit Tracker API")
                .description("""
                **Habit Tracker API** - Sistema de Acompanhamento de Hábitos
                
                Esta API permite gerenciar hábitos pessoais, acompanhar progresso através de
                sequências (streaks) e obter insights sobre o desempenho.
                
                ### Funcionalidades Principais:
                - ✅ **CRUD de Hábitos**: Criar, atualizar, listar e desativar hábitos
                - 📈 **Tracking de Sequências**: Acompanhar dias consecutivos de prática
                - 📊 **Estatísticas**: Métricas de progresso e desempenho
                - 🔄 **Estados**: Ativar/desativar hábitos conforme necessário
                
                ### Arquitetura:
                - **Clean Architecture**: Domínio rico e independente
                - **DDD**: Modelagem orientada ao domínio
                - **SOLID**: Princípios aplicados em todas as camadas
                - **REST**: API RESTful com códigos HTTP apropriados
                
                ### Como usar:
                1. Substitua `{userId}` pelo ID do usuário (UUID)
                2. Use os endpoints para gerenciar hábitos
                3. Acompanhe o progresso através das sequências
                
                ### Códigos de Status:
                - **200**: Sucesso
                - **201**: Recurso criado
                - **400**: Dados inválidos
                - **404**: Recurso não encontrado
                - **409**: Conflito (ex: nome duplicado)
                - **500**: Erro interno
                """)
                .version("1.0.0")
                .contact(apiContact())
                .license(apiLicense());
    }

    private Contact apiContact() {
        return new Contact()
                .name("Luis Henrique")
                .email("luishenriqueaguiar10@gmail.com");
    }

    private License apiLicense() {
        return new License()
                .name("MIT License");
    }

    private Server developmentServer() {
        return new Server()
                .url("http://localhost:" + serverPort)
                .description("Servidor de Desenvolvimento");
    }

    private Server productionServer() {
        return new Server()
                .url("https://api.habittracker.com")
                .description("Servidor de Produção");
    }
}
