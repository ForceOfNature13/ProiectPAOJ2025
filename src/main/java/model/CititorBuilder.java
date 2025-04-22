package model;

import exceptie.InputInvalidExceptie;
import java.util.Objects;

public class CititorBuilder {
    private String nume;
    private String prenume;
    private String email;
    private String telefon;
    private String username;
    private String parola;
    private String adresa;
    private int nrMaxImprumuturi = 3;

    public CititorBuilder nume(String v)      { nume = v;      return this; }
    public CititorBuilder prenume(String v)   { prenume = v;   return this; }
    public CititorBuilder email(String v)     { email = v;     return this; }
    public CititorBuilder telefon(String v)   { telefon = v;   return this; }
    public CititorBuilder username(String v)  { username = v;  return this; }
    public CititorBuilder parola(String v)    { parola = v;    return this; }
    public CititorBuilder adresa(String v)    { adresa = v;    return this; }
    public CititorBuilder nrMax(int v)        { nrMaxImprumuturi = v; return this; }

    public Cititor build() throws InputInvalidExceptie {
        if (Objects.requireNonNullElse(nume,"").isBlank())    throw new InputInvalidExceptie("nume", nume);
        if (Objects.requireNonNullElse(prenume,"").isBlank()) throw new InputInvalidExceptie("prenume", prenume);
        return new Cititor(nume, prenume, email, telefon,
                username, parola, adresa, nrMaxImprumuturi);
    }
}
