package com.dior.dior;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.dior.dior.mapper")
public class DiorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiorApplication.class, args);
    }

}
