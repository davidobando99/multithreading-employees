package com.epam.multithreading.employees.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Salary {

    private String id;
    private String salary;

    @JsonCreator
    public Salary(@JsonProperty("id") String id, @JsonProperty("salary") String salary) {
        this.id = id;
        this.salary = salary;
    }

    public Salary(){

    }

    public String getId() {
        return id;
    }

    public String getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "id='" + id + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }
}
