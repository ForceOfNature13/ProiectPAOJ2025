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

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return this.prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return this.parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
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
        if (!(o instanceof Persoana)) return false;
        Persoana persoana = (Persoana) o;
        return this.id == persoana.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
