package model;

public class Editura implements Identifiable {

    private int    id;
    private String nume;
    private final String tara;


    public Editura(int id, String nume, String tara) {
        if (nume == null || nume.isBlank())
            throw new IllegalArgumentException("Numele editurii nu poate fi gol!");

        this.id   = id;
        this.nume = nume;
        this.tara = tara;
    }

    public Editura(String nume, String tara) {
        this(0, nume, tara);
    }

    @Override
    public Integer getId()          { return this.id; }

    @Override
    public void    setId(Integer id){ this.id = id; }

    public String  nume()           { return nume; }
    public void    setNume(String n){ this.nume = n; }

    public String  tara()           { return tara; }

    @Override
    public String toString() {
        return "Editura{" +
                "id="   + id   +
                ", nume='" + nume + '\'' +
                ", tara='" + tara + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Editura e)) return false;
        return id != 0 && id == e.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }



}
