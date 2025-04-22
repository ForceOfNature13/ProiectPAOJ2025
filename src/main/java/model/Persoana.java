package model;

import java.util.Objects;

public abstract class Persoana {
    private static int idGenerator = 0;

    protected int id;
    protected String nume;
    protected String prenume;
    protected String email;
    protected String telefon;
    protected String username;
    protected String parola;
    private boolean blocat = false;

    public Persoana(String nume, String prenume, String email, String telefon, String username, String parola) {
        this.id = ++idGenerator;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.telefon = telefon;
        this.username = username;
        this.parola = parola;
    }

    public int getId() {
        return this.id;
    }

    public String getNume() {
        return this.nume;
    }

    public String getPrenume() {
        return this.prenume;
    }

    public String getUsername() {
        return this.username;
    }

    public String getParola() {
        return this.parola;
    }

    public boolean getBlocat() {
        return blocat;
    }
    public void setBlocat(boolean blocat) {
        this.blocat = blocat;
    }

    @Override
    public String toString() {
        return "Persoana:\n" +
                "Id: " + this.id + "\n" +
                "Nume: " + this.nume + "\n" +
                "Prenume: " + this.prenume + "\n" +
                "Email: " + this.email + "\n" +
                "Telefon: " + this.telefon + "\n" +
                "Username: " + this.username + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persoana persoana)) return false;
        return this.id == persoana.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
