package com.bolanel.labs.service;

import com.bolanel.labs.Employee;

import java.util.List;

public interface EmployeeService {

    int save(String name, Double salary);

    Employee findById(int id);

    Employee findByName(String name);

    List<Employee> findAll();

    void update(Employee employee);

    void deleteById(int id);
}