package com.bolanel.labs;

import com.bolanel.labs.entity.Employee;
import com.bolanel.labs.repository.EmployeeRepository;
import com.bolanel.labs.repository.EmployeeRepositoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties properties = loadDbProperties();

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            EmployeeRepository repository = new EmployeeRepositoryImpl(connection);

            // CREATE
            Employee newEmployee = new Employee("John Doe", 1000.0);
            int id = repository.save(newEmployee);
            System.out.println("Created employee with id = " + id);

            // READ by id
            Employee found = repository.findById(id);
            System.out.println("Found by id: " + found);

            // READ all
            List<Employee> allEmployees = repository.findAll();
            System.out.println("All employees: " + allEmployees);

            // UPDATE
            found.setSalary(1500.0);
            boolean updated = repository.update(found);
            System.out.println("Updated: " + updated);

            Employee afterUpdate = repository.findById(id);
            System.out.println("After update: " + afterUpdate);

            // DELETE
            repository.deleteById(id);
            System.out.println("Deleted employee with id = " + id);

            Employee afterDelete = repository.findById(id);
            System.out.println("After delete (should be null): " + afterDelete);

        } catch (SQLException e) {
            System.err.println("Failed to connect to database");
            e.printStackTrace();
        }
    }

    private static Properties loadDbProperties() {
        Properties properties = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new IllegalStateException("application.properties not found in resources");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DB properties", e);
        }
        return properties;
    }
}