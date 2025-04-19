package model;

public record Editura(int id, String nume, String tara) {
    public Editura {
        if (nume == null || nume.isBlank()) {
            throw new IllegalArgumentException("Numele editurii nu poate fi gol!");
        }
    }
}
