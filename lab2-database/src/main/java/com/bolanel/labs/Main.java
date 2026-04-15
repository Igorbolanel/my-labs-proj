package com.bolanel.labs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Properties properties = loadDbProperties();

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            EmployeeRepository repository = new EmployeeRepositoryImpl(connection);

            Employee newEmployee = new Employee("John Doe", 1000.0);
            int id = repository.save(newEmployee);
            log.info("Created employee with id = {}", id);

            Employee found = repository.findById(id);
            log.info("Found by id: {}", found);

            List<Employee> allEmployees = repository.findAll();
            log.info("All employees: {}", allEmployees);

            found.setSalary(1500.0);
            boolean updated = repository.update(found);
            log.info("Updated: {}", updated);

            Employee afterUpdate = repository.findById(id);
            log.info("After update: {}", afterUpdate);

            repository.deleteById(id);
            log.info("Deleted employee with id = {}", id);

            Employee afterDelete = repository.findById(id);
            log.info("After delete (should be null): {}", afterDelete);

        } catch (SQLException e) {
            log.error("Failed to connect to database", e);
        }
    }

    private static Properties loadDbProperties() {
        Properties properties = new Properties();
        try (InputStream is = Main.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
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