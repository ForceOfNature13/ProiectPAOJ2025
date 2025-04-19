package model;

import java.time.LocalDate;

public class Bibliotecar extends Persoana {
    private String sectie;
    private LocalDate dataAngajare;
    private RolBibliotecar rol;

    public Bibliotecar(String nume,
                       String prenume,
                       String email,
                       String telefon,
                       String username,
                       String parola,
                       String sectie,
                       LocalDate dataAngajare) {
        super(nume, prenume, email, telefon, username, parola);
        this.sectie = sectie;
        this.dataAngajare = dataAngajare;
        this.rol = rol;
    }

    public String getSectie() {
        return this.sectie;
    }

    public void setSectie(String sectie) {
        this.sectie = sectie;
    }

    public LocalDate getDataAngajare() {
        return this.dataAngajare;
    }

    public void setDataAngajare(LocalDate dataAngajare) {
        this.dataAngajare = dataAngajare;
    }

    public RolBibliotecar getRol() {
        return this.rol;
    }

    public void setRol(RolBibliotecar rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Bibliotecar:\n" +
                super.toString() +
                "Sectie: " + this.sectie + "\n" +
                "Data angajare: " + this.dataAngajare + "\n" +
                "Rol: " + this.rol + "\n";
    }
}
