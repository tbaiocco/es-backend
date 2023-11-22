package io.vicarius.esbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development environment");

        Contact contact = new Contact();
        contact.setEmail("tbaiocco@gmail.com");
        contact.setName("Teofilo Baiocco");
        contact.setUrl("https://github.com/tbaiocco/es-backend");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Simple Elasticsearch API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to test the Backend Exam for Vicarius")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
