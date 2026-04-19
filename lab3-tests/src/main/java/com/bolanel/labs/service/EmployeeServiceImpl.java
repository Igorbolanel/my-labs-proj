package com.bolanel.labs.service;

import com.bolanel.labs.Employee;
import com.bolanel.labs.EmployeeRepository;
import com.bolanel.labs.exception.EmployeeNotFoundException;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public int save(String name, Double salary) {
        validateName(name);
        validateSalary(salary);

        Employee employee = new Employee(name, salary);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee findById(int id) {
        validateId(id);

        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }

        return employee;
    }

    @Override
    public Employee findByName(String name) {
        validateName(name);

        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .filter(employee -> employee.getName() != null)
                .filter(employee -> employee.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee with name '" + name + "' not found"));
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void update(Employee employee) {
        validateEmployee(employee);

        if (employee.getId() == null || employee.getId() <= 0) {
            throw new IllegalArgumentException("Employee id must be positive");
        }

        Employee existingEmployee = employeeRepository.findById(employee.getId());
        if (existingEmployee == null) {
            throw new EmployeeNotFoundException("Employee with id " + employee.getId() + " not found");
        }

        boolean updated = employeeRepository.update(employee);
        if (!updated) {
            throw new RuntimeException("Failed to update employee with id " + employee.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        validateId(id);

        Employee existingEmployee = employeeRepository.findById(id);
        if (existingEmployee == null) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }

        employeeRepository.deleteById(id);
    }

    private void validateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee must not be null");
        }
        validateName(employee.getName());
        validateSalary(employee.getSalary());
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Employee name must not be null or blank");
        }
    }

    private void validateSalary(Double salary) {
        if (salary == null || salary < 0) {
            throw new IllegalArgumentException("Employee salary must not be null or negative");
        }
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Employee id must be positive");
        }
    }
}