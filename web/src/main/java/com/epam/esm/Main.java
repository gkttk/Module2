package com.epam.esm;

import com.epam.esm.config.DaoConfig;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication/*(scanBasePackageClasses = {*//*WebConfig.class, *//*ServiceConfig.class, DaoConfig.class})*/
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}

