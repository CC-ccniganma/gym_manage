package com.milotnt;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling

public class
GymManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymManagementSystemApplication.class, args);
    }
}
