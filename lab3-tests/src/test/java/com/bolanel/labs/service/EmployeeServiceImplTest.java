package com.bolanel.labs.service;

import com.bolanel.labs.Employee;
import com.bolanel.labs.EmployeeRepository;
import com.bolanel.labs.exception.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    void create_shouldSaveAndReturnEmployee() {
        Employee employee = new Employee("Igor", 1000.0);
        when(employeeRepository.save(employee)).thenReturn(1);

        Employee result = employeeService.create(employee);

        assertEquals(1, result.getId());
        assertSame(employee, result);
        verify(employeeRepository).save(employee);
    }

    @Test
    void findById_shouldReturnEmployeeWhenExists() {
        Employee employee = new Employee(1, "Igor", 1000.0);
        when(employeeRepository.findById(1)).thenReturn(employee);

        Employee result = employeeService.findById(1);

        assertEquals(employee, result);
        verify(employeeRepository).findById(1);
    }

    @Test
    void findById_shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findById(1)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findById(1));
        verify(employeeRepository).findById(1);
    }

    @Test
    void findAll_shouldReturnAllEmployees() {
        List<Employee> employees = List.of(
                new Employee(1, "Igor", 1000.0),
                new Employee(2, "Anna", 2000.0)
        );
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.findAll();

        assertEquals(employees, result);
        verify(employeeRepository).findAll();
    }

    @Test
    void update_shouldUpdateAndReturnEmployeeWhenExists() {
        Employee employee = new Employee(1, "Igor", 1500.0);
        when(employeeRepository.findById(1)).thenReturn(employee);
        when(employeeRepository.update(employee)).thenReturn(true);

        Employee result = employeeService.update(employee);

        assertEquals(employee, result);
        verify(employeeRepository).findById(1);
        verify(employeeRepository).update(employee);
    }

    @Test
    void update_shouldThrowExceptionWhenEmployeeNotFound() {
        Employee employee = new Employee(1, "Igor", 1500.0);
        when(employeeRepository.findById(1)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(employee));
        verify(employeeRepository).findById(1);
    }

    @Test
    void deleteById_shouldDeleteWhenEmployeeExists() {
        Employee employee = new Employee(1, "Igor", 1000.0);
        when(employeeRepository.findById(1)).thenReturn(employee);

        employeeService.deleteById(1);

        verify(employeeRepository).findById(1);
        verify(employeeRepository).deleteById(1);
    }

    @Test
    void deleteById_shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findById(1)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteById(1));
        verify(employeeRepository).findById(1);
    }

    @Test
    void findByName_shouldReturnEmployeeWhenExists() {
        Employee employee = new Employee(1, "Igor", 1000.0);
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        Employee result = employeeService.findByName("Igor");

        assertEquals(employee, result);
        verify(employeeRepository).findAll();
    }

    @Test
    void findByName_shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findByName("Igor"));
        verify(employeeRepository).findAll();
    }
}