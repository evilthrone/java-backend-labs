package ru.kultishov.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class StatusCountWriter {
    public static void write(Path outputFile, Map<String, Integer> statusCounts) {
        if (statusCounts.isEmpty()) throw new IllegalArgumentException("Мапа пуста");

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
                writer.write(entry.getKey() + ", " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи файла: " + outputFile, e);
        }
    }
}
