package com.epam.multithreading.employees.service;

import com.epam.multithreading.employees.delegate.EmployeeDataClient;
import com.epam.multithreading.employees.delegate.EmployeeSalaryClient;
import com.epam.multithreading.employees.model.Employee;
import com.epam.multithreading.employees.model.HiredEmployee;
import com.epam.multithreading.employees.model.Salary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FetchEmployeeDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchEmployeeDataService.class);

    @Autowired
    private EmployeeDataClient employeeDataClient;

    @Autowired
    private EmployeeSalaryClient employeeSalaryClient;

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public List<Employee> fetchEmployeeData(List<String> employeeIds){
        List<CompletableFuture<Employee>> future = employeeIds.stream()
                .map(this::fetchEmployeeDataAsync)
                .collect(Collectors.toList());
        return future.stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private CompletableFuture<Employee> fetchEmployeeDataAsync(String employeeId) {
        return CompletableFuture.supplyAsync(() -> {

            List<Object> result = Stream.of(fetchHiredEmployees(employeeId), fetchSalaryData(employeeId))
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            HiredEmployee hiredEmployee = (HiredEmployee) result.get(0);
            Salary salary = (Salary) result.get(1);
            LOGGER.info(hiredEmployee.toString());
            return new Employee(hiredEmployee.getId(),hiredEmployee.getName(), hiredEmployee.getTitle(),salary.getSalary());
        }, executor).exceptionally(ex -> {
            LOGGER.warn("There is an exception {} ",ex.getMessage());
            return new Employee();
        });
    }

    public CompletableFuture<HiredEmployee> fetchHiredEmployees(String employeeId){
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("fetchHiredEmployees {} ", Thread.currentThread().getName());
            return employeeDataClient.getHiredEmployee(employeeId);
                        },executor)
                .exceptionally(ex -> {
                    LOGGER.warn("There is an exception {} ",ex.getMessage());
                    return new HiredEmployee();
                });
    }

    public CompletableFuture<Salary> fetchSalaryData(String employeeId){
        return CompletableFuture.supplyAsync(() -> {
                    LOGGER.info("fetchSalaryData {} ", Thread.currentThread().getName());
                    return employeeSalaryClient.getSalary(employeeId);
                },executor)
                .exceptionally(ex -> {
                    LOGGER.warn("There is an exception {} ",ex.getMessage());
                    return new Salary();
                });
    }




}
