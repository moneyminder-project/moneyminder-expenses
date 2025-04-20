package com.moneyminder.moneyminderexpenses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@AutoConfiguration
@SpringBootApplication
public class MoneyMinderExpensesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyMinderExpensesApplication.class, args);
    }

}
