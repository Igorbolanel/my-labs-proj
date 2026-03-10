package com.bolanel.labs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    private static final Logger log = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

    private final Connection connection;

    public EmployeeRepositoryImpl(Connection connection) {
        this.connection = connection;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS employee
                (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    salary DOUBLE PRECISION NOT NULL
                );
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            log.info("Table employee is ready");
        } catch (SQLException e) {
            log.error("Failed to create table employee", e);
            throw new RuntimeException("Failed to create table employee", e);
        }
    }

    @Override
    public int save(Employee employee) {
        String sql = "INSERT INTO employee (name, salary) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, employee.getName());
            ps.setDouble(2, employee.getSalary());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Saving employee failed, no rows affected");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    employee.setId(id);
                    log.debug("Generated id = {} for employee {}", id, employee.getName());
                    return id;
                } else {
                    throw new RuntimeException("Saving employee failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            log.error("Failed to save employee", e);
            throw new RuntimeException("Failed to save employee", e);
        }
    }

    @Override
    public Employee findById(int id) {
        String sql = "SELECT id, name, salary FROM employee WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            log.error("Failed to find employee by id {}", id, e);
            throw new RuntimeException("Failed to find employee by id " + id, e);
        }
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT id, name, salary FROM employee";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Employee> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            log.error("Failed to find all employees", e);
            throw new RuntimeException("Failed to find all employees", e);
        }
    }

    @Override
    public boolean update(Employee employee) {
        if (employee.getId() == null) {
            return false;
        }

        String sql = "UPDATE employee SET name = ?, salary = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, employee.getName());
            ps.setDouble(2, employee.getSalary());
            ps.setInt(3, employee.getId());

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            log.error("Failed to update employee with id {}", employee.getId(), e);
            throw new RuntimeException("Failed to update employee with id " + employee.getId(), e);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM employee WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to delete employee with id {}", id, e);
            throw new RuntimeException("Failed to delete employee with id " + id, e);
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double salary = rs.getDouble("salary");
        return new Employee(id, name, salary);
    }
}