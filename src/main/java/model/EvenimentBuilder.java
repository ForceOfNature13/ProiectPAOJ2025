package model;

import exceptie.InputInvalidExceptie;
import java.time.LocalDate;

public class EvenimentBuilder {
    private String titlu;
    private String descriere;
    private LocalDate data;
    private String locatie;
    private int capacitate;

    public EvenimentBuilder titlu(String t)        { this.titlu = t; return this; }
    public EvenimentBuilder descriere(String d)    { this.descriere = d; return this; }
    public EvenimentBuilder data(LocalDate d)      { this.data = d; return this; }
    public EvenimentBuilder locatie(String l)      { this.locatie = l; return this; }
    public EvenimentBuilder capacitate(int c)      { this.capacitate = c; return this; }

    public Eveniment build() throws InputInvalidExceptie {
        if (titlu == null || titlu.isBlank())       throw new InputInvalidExceptie("titlu", titlu);
        if (descriere == null || descriere.isBlank()) throw new InputInvalidExceptie("descriere", descriere);
        if (data == null || !data.isAfter(LocalDate.now()))
            throw new InputInvalidExceptie("data (trebuie sa fie in viitor)", data);
        if (locatie == null || locatie.isBlank())  throw new InputInvalidExceptie("locatie", locatie);
        if (capacitate <= 0)                       throw new InputInvalidExceptie("capacitate", capacitate);

        return new Eveniment(titlu, descriere, data, locatie, capacitate);
    }
}
