package com.bolanel.labs.service;

import com.bolanel.labs.Employee;

import java.util.List;

public interface EmployeeService {

    Employee create(Employee employee);

    Employee findById(Integer id);

    List<Employee> findAll();

    Employee findByName(String name);
}