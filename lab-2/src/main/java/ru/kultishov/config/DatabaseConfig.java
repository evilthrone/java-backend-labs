package ru.kultishov.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

public record DatabaseConfig(String url, String username, String password, String schema) {
    public static DatabaseConfig load() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        return new DatabaseConfig(
                required(dotenv, "DATABASE_URL"),
                required(dotenv, "DATABASE_USERNAME"),
                required(dotenv, "DATABASE_PASSWORD"),
                required(dotenv, "APP_DATABASE_SCHEMA")
        );
    }

    public HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setSchema(schema);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(5000);
        return new HikariDataSource(config);
    }

    private static String required(Dotenv dotenv, String name) {
        String value = dotenv.get(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Не задана настройка " + name + " в .env или окружении");
        }
        return value;
    }
}
