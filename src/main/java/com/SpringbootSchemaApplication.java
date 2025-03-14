package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.dao")
public class SpringbootSchemaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootSchemaApplication.class, args);
    }
}
