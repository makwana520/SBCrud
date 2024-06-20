package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@SpringBootApplication
public class Demo5Application {

    private static final String UPLOAD_DIR = "uploads/";

    public static void main(String[] args) {
        SpringApplication.run(Demo5Application.class, args);
    }

    @PostConstruct
    public void init() {
        try {
           
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }
}
