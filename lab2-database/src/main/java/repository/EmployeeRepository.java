package com.bolanel.labs.repository;

import com.bolanel.labs.entity.Employee;

import java.util.List;

public interface EmployeeRepository {

    int save(Employee employee);

    Employee findById(int id);

    List<Employee> findAll();

    boolean update(Employee employee);

    void deleteById(int id);
}