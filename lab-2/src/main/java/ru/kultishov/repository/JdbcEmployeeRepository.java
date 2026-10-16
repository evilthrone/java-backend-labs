package ru.kultishov.repository;

import ru.kultishov.exception.RepositoryException;
import ru.kultishov.model.Employee;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcEmployeeRepository implements EmployeeRepository {
    private final DataSource dataSource;

    public JdbcEmployeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int save(Employee employee) {
        String sql = "INSERT INTO employee (name, salary) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"})) {
            statement.setString(1, employee.name());
            statement.setDouble(2, employee.salary());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("База данных не вернула id сотрудника");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Не удалось сохранить сотрудника", e);
        }
    }

    @Override
    public Employee findById(int id) {
        String sql = "SELECT id, name, salary FROM employee WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readEmployee(result) : null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Не удалось найти сотрудника по id", e);
        }
    }

    @Override
    public Employee findByName(String name) {
        String sql = "SELECT id, name, salary FROM employee WHERE name = ? ORDER BY id LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readEmployee(result) : null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Не удалось найти сотрудника по имени", e);
        }
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT id, name, salary FROM employee ORDER BY id";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            List<Employee> employees = new ArrayList<>();
            while (result.next()) {
                employees.add(readEmployee(result));
            }
            return employees;
        } catch (SQLException e) {
            throw new RepositoryException("Не удалось получить список сотрудников", e);
        }
    }

    @Override
    public boolean update(Employee employee) {
        String sql = "UPDATE employee SET name = ?, salary = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employee.name());
            statement.setDouble(2, employee.salary());
            statement.setObject(3, employee.id());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException("Не удалось обновить сотрудника", e);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Не удалось удалить сотрудника", e);
        }
    }

    private Employee readEmployee(ResultSet result) throws SQLException {
        return new Employee(result.getInt("id"), result.getString("name"), result.getDouble("salary"));
    }
}
