package ru.kultishov;

import ru.kultishov.service.CharacterStatusService;
import ru.kultishov.writer.StatusCountWriter;

import java.nio.file.Path;
import java.util.Map;

// ВАРИАНТ 4
public class Main {
    public static void main(String[] args) {
        Path inputFile = Path.of("lab-1/src/main/resources/characters.csv");
        Path outputFile = Path.of("lab-1/src/main/resources/status-counts.csv");

        Map<String, Integer> statusCounts = CharacterStatusService.countByStatus(inputFile);
        StatusCountWriter.write(outputFile, statusCounts);

        System.out.println("Выходной файл успешно создан по пути: " + outputFile.toAbsolutePath());
    }

}
