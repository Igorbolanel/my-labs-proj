package com.bolanel.labs.service;

import com.bolanel.labs.Employee;
import com.bolanel.labs.EmployeeRepository;
import com.bolanel.labs.exception.EmployeeNotFoundException;

import java.util.List;
import java.util.Objects;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee create(Employee employee) {
        validateEmployee(employee);
        employeeRepository.save(employee);
        return employee;
    }

    @Override
    public Employee findById(Integer id) {
        validateId(id);

        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }

        return employee;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Employee name must not be null or blank");
        }

        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .filter(Objects::nonNull)
                .filter(employee -> employee.getName() != null)
                .filter(employee -> employee.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee with name '" + name + "' not found"));
    }

    private void validateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee must not be null");
        }

        if (employee.getName() == null || employee.getName().isBlank()) {
            throw new IllegalArgumentException("Employee name must not be null or blank");
        }

        if (employee.getSalary() == null || employee.getSalary() < 0) {
            throw new IllegalArgumentException("Employee salary must not be null or negative");
        }
    }

    private void validateId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Employee id must be positive");
        }
    }
}