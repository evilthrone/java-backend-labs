package ru.kultishov;

import ru.kultishov.model.Character;
import ru.kultishov.repository.CharacterRepository;
import ru.kultishov.service.CharacterStatusService;
import ru.kultishov.writer.StatusCountWriter;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;

// ВАРИАНТ 4
public class Main {
    public static void main(String[] args) {
        Path inputFile = Path.of("lab-1/src/main/resources/characters.csv");
        Path outputFile = Path.of("lab-1/src/main/resources/status-counts.csv");
        CharacterRepository repository = new CharacterRepository(inputFile);
        CharacterStatusService service = new CharacterStatusService(repository);

        Map<String, Integer> statusCounts = service.countByStatus();
        StatusCountWriter.write(outputFile, statusCounts);

        System.out.println("Выходной файл успешно создан по пути: " + outputFile.toAbsolutePath());

        demonstrateCrud(repository);
    }

    private static void demonstrateCrud(CharacterRepository repository) {
        int id = 0;
        for (Character character : repository.readAll()) {
            id = Math.max(id, character.id());
        }
        id++;

        Character character = new Character(id, "Test Rick", "Alive", "Human", "", "Male",
                "Earth", "Earth", Instant.now().toString());

        repository.create(character);
        System.out.println("Добавлен: " + character.toCsv());

        Character updated = new Character(character.id(), character.name(), "Dead", character.species(),
                character.type(), character.gender(), character.origin(), character.location(), character.created());
        repository.update(updated);
        System.out.println("Обновлён: " + updated.toCsv());

        repository.deleteById(id);
        System.out.println("Удалён персонаж с ID " + id);
    }
}
