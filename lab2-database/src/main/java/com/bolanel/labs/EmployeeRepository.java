package com.bolanel.labs;

import java.util.List;

public interface EmployeeRepository {

    int save(Employee employee);

    Employee findById(int id);

    List<Employee> findAll();

    boolean update(Employee employee);

    void deleteById(int id);
}