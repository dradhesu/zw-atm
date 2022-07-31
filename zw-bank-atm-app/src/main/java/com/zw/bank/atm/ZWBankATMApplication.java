package com.zw.bank.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {"com.zw.bank"})
@EnableJpaRepositories
public class ZWBankATMApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZWBankATMApplication.class, args);
    }

    /**
     * Setting JVM default timezone to UTC
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
