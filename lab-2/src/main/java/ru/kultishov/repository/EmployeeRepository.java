package ru.kultishov.repository;

import ru.kultishov.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    int save(Employee employee);

    Employee findById(int id);

    Employee findByName(String name);

    List<Employee> findAll();

    boolean update(Employee employee);

    void deleteById(int id);
}
