package com.bolanel.labs.service;

import com.bolanel.labs.Employee;

import java.util.List;

public interface EmployeeService {

    Employee create(Employee employee);

    Employee findById(int id);

    List<Employee> findAll();

    Employee update(Employee employee);

    void deleteById(int id);

    Employee findByName(String name);
}