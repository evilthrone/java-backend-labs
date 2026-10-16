package ru.kultishov;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kultishov.config.DatabaseConfig;
import ru.kultishov.model.Employee;
import ru.kultishov.repository.EmployeeRepository;
import ru.kultishov.repository.JdbcEmployeeRepository;

import java.util.UUID;

// Вариант 4: Employee (id, name, salary).
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.load();
        try (HikariDataSource dataSource = config.createDataSource()) {
            Flyway.configure()
                    .dataSource(dataSource)
                    .schemas(config.schema())
                    .defaultSchema(config.schema())
                    .load()
                    .migrate();

            EmployeeRepository repository = new JdbcEmployeeRepository(dataSource);
            demonstrateCrud(repository);
        }
    }

    private static void demonstrateCrud(EmployeeRepository repository) {
        // Уникальное имя позволяет отличить демонстрационную запись от существующих.
        String name = "Demo employee " + UUID.randomUUID();
        int id = repository.save(new Employee(null, name, 80_000));
        log.info("save: создан сотрудник с id={}", id);
        log.info("findById: {}", repository.findById(id));
        log.info("findByName: {}", repository.findByName(name));
        log.info("findAll: {}", repository.findAll());

        Employee updated = new Employee(id, name, 95_000);
        log.info("update: {}", repository.update(updated));
        log.info("Повторный update: {}", repository.update(updated));
        log.info("После обновления: {}", repository.findById(id));

        repository.deleteById(id);
        repository.deleteById(id);
        log.info("После двух deleteById: {}", repository.findById(id));
        log.info("Поиск по имени после удаления: {}", repository.findByName(name));
        log.info("update отсутствующего сотрудника: {}", repository.update(updated));
    }
}
