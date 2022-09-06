package com.epam.multithreading.employees.model;

public class Employee {

    private String id;
    private String name;
    private String title;
    private String salary;

    public Employee(String id, String name, String title, String salary) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.salary = salary;
    }

    public Employee() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getSalary() {
        return salary;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }
}
