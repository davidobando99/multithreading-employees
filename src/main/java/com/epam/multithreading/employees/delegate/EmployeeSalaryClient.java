package com.epam.multithreading.employees.delegate;

import com.epam.multithreading.employees.domain.Salary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "salary-client", url = "https://631620f95b85ba9b11f08723.mockapi.io/employeeapi/v1/salary")
public interface EmployeeSalaryClient {

    @GetMapping("/{hiredEmployeeId}")
    public Salary getSalary(@PathVariable("hiredEmployeeId") String hiredEmployeeId);
}
