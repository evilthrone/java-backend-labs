package ru.kultishov.model;

public record Character(int id, String name, String status, String species, String type,
                        String gender, String origin, String location, String created) {

    public String toCsv() {
        String[] fields = {String.valueOf(id), name, status, species, type, gender, origin, location, created};
        for (String field : fields) {
            if (field == null) {
                throw new IllegalArgumentException("Поле CSV не должно быть null");
            }
        }
        return String.join(",", fields);
    }
}
