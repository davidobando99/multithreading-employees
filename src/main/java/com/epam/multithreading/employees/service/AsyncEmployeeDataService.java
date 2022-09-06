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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AsyncEmployeeDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEmployeeDataService.class);

    @Autowired
    private EmployeeDataClient employeeDataClient;

    @Autowired
    private EmployeeSalaryClient employeeSalaryClient;

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public List<Employee> fetchEmployeeDataAsync() throws ExecutionException, InterruptedException {
            CompletableFuture<List<Employee>> employees = fetchHiredEmployees().thenComposeAsync(hiredEmployees -> {
                List<CompletableFuture<Employee>> list = hiredEmployees.stream().map(this::employeeCompletedWithSalary).collect(Collectors.toList());
                CompletableFuture<Void> allFuturesResult =
                        CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
                return allFuturesResult.thenApply(v ->
                        list.stream().
                                map(CompletableFuture::join)
                                .collect(Collectors.toList())
                );
            });
            return employees.get();
    }

    private CompletableFuture<Employee> employeeCompletedWithSalary(HiredEmployee employee){
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("fetchEmployeesWithSalary {} ", Thread.currentThread().getName());
            List<Object> result = Stream.of(CompletableFuture.supplyAsync(() -> employee), fetchSalaryData(employee.getId()))
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            HiredEmployee hiredEmployee = (HiredEmployee) result.get(0);
            Salary salary = (Salary) result.get(1);
            LOGGER.info(hiredEmployee.toString());
            return new Employee(hiredEmployee.getId(),hiredEmployee.getName(), hiredEmployee.getTitle(),salary.getSalary());
        });
    }

    private CompletableFuture<List<HiredEmployee>> fetchHiredEmployees(){
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("fetchHiredEmployees {} ", Thread.currentThread().getName());
            return employeeDataClient.hiredEmployees();
                        },executor)
                .exceptionally(ex -> {
                    LOGGER.warn("There is an exception {} ",ex.getMessage());
                    return new ArrayList<>();
                });
    }

    private CompletableFuture<Salary> fetchSalaryData(String employeeId){
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
