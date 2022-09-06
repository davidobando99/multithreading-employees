package com.epam.multithreading.employees;

import com.epam.multithreading.employees.model.Employee;
import com.epam.multithreading.employees.service.AsyncEmployeeDataService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(AsyncEmployeeDataService employeeService) {
        return args -> {
            StopWatch watch = new StopWatch();
            watch.start();
            List<Employee> employees = employeeService.fetchEmployeeDataAsync();
            watch.stop();
            LOGGER.info("Time elapsed {} ",watch.getTime()+" ms");
            LOGGER.info("Employees {} ",employees);
        };
    }
}
