package ru.kultishov.repository;

import ru.kultishov.model.Character;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CharacterRepository {
    private final Path file;

    public CharacterRepository(Path file) {
        this.file = file;
    }

    public List<Character> readAll() {
        List<Character> characters = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            if (reader.readLine() == null) throw new IllegalArgumentException("Файл пуст");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length != 9) {
                    throw new IllegalArgumentException("Ожидалось 9 полей CSV: " + line);
                }
                Character character = new Character(Integer.parseInt(
                        fields[0]),
                        fields[1],
                        fields[2],
                        fields[3],
                        fields[4],
                        fields[5],
                        fields[6],
                        fields[7],
                        fields[8]);

                characters.add(character);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + file, e);
        }
        return characters;
    }

    public void create(Character character) {
        List<Character> characters = readAll();
        for (Character existing : characters) {
            if (existing.id() == character.id()) {
                throw new IllegalArgumentException("Персонаж с ID " + character.id() + " уже существует");
            }
        }
        characters.add(character);
        saveAll(characters);
    }

    public void update(Character character) {
        List<Character> characters = readAll();
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).id() == character.id()) {
                characters.set(i, character);
                saveAll(characters);
                return;
            }
        }
        throw new IllegalArgumentException("Персонаж с ID " + character.id() + " не найден");
    }

    public void deleteById(int id) {
        List<Character> characters = readAll();
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).id() == id) {
                characters.remove(i);
                saveAll(characters);
                return;
            }
        }
        throw new IllegalArgumentException("Персонаж с ID " + id + " не найден");
    }

    // Полностью перезаписывает исходный CSV файл с новыми данными
    private void saveAll(List<Character> characters) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,status,species,type,gender,origin/name,location/name,created");
        for (Character character : characters) {
            lines.add(character.toCsv());
        }
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи файла: " + file, e);
        }
    }
}
