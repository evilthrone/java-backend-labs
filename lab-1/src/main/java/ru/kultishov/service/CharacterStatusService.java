package ru.kultishov.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CharacterStatusService {
    public Map<String, Integer> countByStatus(Path inputFile) {
        HashMap<String, Integer> statusCounts = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                String status = fields[2];

                statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + inputFile, e);
        }

        return statusCounts;
    }
}
