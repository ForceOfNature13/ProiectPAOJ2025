package model;

import java.util.Objects;

public abstract class Persoana implements Identifiable {

    protected int id;
    protected String nume;
    protected String prenume;
    protected String email;
    protected String telefon;
    protected String username;
    protected String parola;
    private   boolean blocat = false;

    public Persoana(String nume,
                    String prenume,
                    String email,
                    String telefon,
                    String username,
                    String parola) {

        this.id       = 0;
        this.nume     = nume;
        this.prenume  = prenume;
        this.email    = email;
        this.telefon  = telefon;
        this.username = username;
        this.parola   = parola;
    }

    @Override
    public Integer getId()        { return id; }
    @Override
    public void    setId(Integer id) { this.id = id; }

    public String  getNume()      { return nume; }
    public String  getPrenume()   { return prenume; }
    public String  getEmail()     { return email; }
    public String  getTelefon()   { return telefon; }
    public String  getUsername()  { return username; }
    public String  getParola()    { return parola; }

    public boolean getBlocat()    { return blocat; }
    public void    setBlocat(boolean blocat) { this.blocat = blocat; }
    public void    setTelefon(String number) { this.telefon = number; }

    @Override
    public String toString() {
        return "Persoana:\n" +
                "Id: "       + id       + "\n" +
                "Nume: "     + nume     + "\n" +
                "Prenume: "  + prenume  + "\n" +
                "Email: "    + email    + "\n" +
                "Telefon: "  + telefon  + "\n" +
                "Username: " + username + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)                   return true;
        if (!(o instanceof Persoana p))  return false;

        return id != 0 && id == p.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
