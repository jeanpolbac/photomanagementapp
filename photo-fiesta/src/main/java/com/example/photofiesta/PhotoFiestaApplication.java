package com.example.photofiesta;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;



@SpringBootApplication
public class PhotoFiestaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoFiestaApplication.class, args);
    }
    @Bean
    public OpenAPI openApiInformation() {
        Server localServer = new Server().url("http://localhost:9095").description("Localhost Server URL");
        Contact contact = new Contact().email("ashleymshakir@gmail.com").name("Ashley Shakir, JP Bacquet, Rick Maya");
        Info info = new Info().contact(contact).description("Photo Management Rest API")
                .summary("GA Unit 2 Spring Boot Project").title("Photo Fiesta API")
                .version("V1.0.0");

        return new OpenAPI().info(info).addServersItem(localServer);
    }

}
