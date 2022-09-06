package com.epam.multithreading.employees.delegate;

import com.epam.multithreading.employees.model.HiredEmployee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "hired-employees-client", url = "https://631620f95b85ba9b11f08723.mockapi.io/employeeapi/v1/hiredemployees")
public interface EmployeeDataClient {


    @GetMapping("/")
    public List<HiredEmployee> hiredEmployees();

    @GetMapping("/{employeeId}")
    public HiredEmployee getHiredEmployee(@PathVariable("employeeId") String employeeId);





}
