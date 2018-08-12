package com.scbb.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackageClasses = {BankApplication.class/*, Jsr310JpaConverters.class*/})
public class BankApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+5:30"));
    }

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

}
