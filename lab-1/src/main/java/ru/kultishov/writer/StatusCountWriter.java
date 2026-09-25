package ru.kultishov.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class StatusCountWriter {
    public void write(Path outputFile, Map<String, Integer> statusCounts) {
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
