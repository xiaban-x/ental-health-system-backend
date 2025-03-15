package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.dao")
public class SpringBootSchemaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSchemaApplication.class, args);
    }
}
