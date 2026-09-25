package ru.kultishov.service;

import ru.kultishov.model.Character;
import ru.kultishov.repository.CharacterRepository;

import java.util.HashMap;
import java.util.Map;

public class CharacterStatusService {
    private final CharacterRepository repository;

    public CharacterStatusService(CharacterRepository repository) {
        this.repository = repository;
    }

    public Map<String, Integer> countByStatus() {
        HashMap<String, Integer> statusCounts = new HashMap<>();

        for (Character character : repository.readAll()) {
            String status = character.status();
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
        }

        return statusCounts;
    }
}
