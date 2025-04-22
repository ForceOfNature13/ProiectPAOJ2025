package model;

import exceptie.InputInvalidExceptie;
import java.time.LocalDate;
import java.util.Objects;

public class BibliotecarBuilder {
    private String nume;
    private String prenume;
    private String email;
    private String telefon;
    private String username;
    private String parola;
    private String sectie;
    private LocalDate dataAngajare = LocalDate.now();

    public BibliotecarBuilder nume(String v){ nume=v; return this; }
    public BibliotecarBuilder prenume(String v){ prenume=v; return this; }
    public BibliotecarBuilder email(String v){ email=v; return this; }
    public BibliotecarBuilder telefon(String v){ telefon=v; return this; }
    public BibliotecarBuilder username(String v){ username=v; return this; }
    public BibliotecarBuilder parola(String v){ parola=v; return this; }
    public BibliotecarBuilder sectie(String v){ sectie=v; return this; }
    public BibliotecarBuilder data(LocalDate d){ dataAngajare=d; return this; }

    public Bibliotecar build() throws InputInvalidExceptie {
        if (Objects.requireNonNullElse(nume,"").isBlank()) throw new InputInvalidExceptie("nume", nume);
        if (Objects.requireNonNullElse(sectie,"").isBlank()) throw new InputInvalidExceptie("sectie", sectie);
        return new Bibliotecar(nume, prenume, email, telefon,
                username, parola, sectie, dataAngajare);
    }
}
