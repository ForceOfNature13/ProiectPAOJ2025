package model;

import java.util.List;
import java.util.Objects;

public abstract class Publicatie implements Imprumutabil, Identifiable {

    protected int id;
    protected String titlu;
    protected int    anPublicare;
    protected int    nrPagini;
    protected boolean disponibil;
    protected int    nrImprumuturi;
    protected List<Recenzie> listaRecenzii;
    protected String categorie;
    protected double rating;

    protected List<String> autori;

    public Publicatie(String titlu,
                      int    anPublicare,
                      int    nrPagini,
                      boolean disponibil,
                      List<Recenzie> listaRecenzii,
                      String categorie,
                      List<String> autori) {

        this.id            = 0;
        this.titlu         = titlu;
        this.anPublicare   = anPublicare;
        this.nrPagini      = nrPagini;
        this.disponibil    = disponibil;
        this.nrImprumuturi = 0;
        this.listaRecenzii = listaRecenzii;
        this.categorie     = categorie;
        this.autori        = autori;
        this.rating        = calculeazaRatingIntern();
    }

    @Override
    public Integer getId()            { return id; }
    @Override
    public void    setId(Integer id)  { this.id = id; }

    public String  getTitlu()         { return titlu; }
    public int     getAnPublicare()   { return anPublicare; }
    public int     getNrPagini()      { return nrPagini; }
    public boolean isDisponibil()     { return disponibil; }
    public void    setDisponibil(boolean d) { disponibil = d; }
    public int     getNrImprumuturi() { return nrImprumuturi; }
    public void    setNrImprumuturi(int n){ nrImprumuturi = n; }
    public List<Recenzie> getListaRecenzii() { return listaRecenzii; }
    public String  getCategorie()     { return categorie; }
    public List<String> getAutori()   { return autori; }
    public double  getRating()        { return rating; }
    public void    setRating(double r){ rating = r; }

    private double calculeazaRatingIntern() {
        if (listaRecenzii == null || listaRecenzii.isEmpty()) return 0;
        return listaRecenzii.stream()
                .mapToInt(Recenzie::getRating)
                .average()
                .orElse(0);
    }

    public void updateRating() { rating = calculeazaRatingIntern(); }

    @Override
    public String toString() {
        return "Publicatie:\n" +
                "Id: "            + id            + "\n" +
                "Titlu: "         + titlu         + "\n" +
                "An publicare: "  + anPublicare   + "\n" +
                "Nr pagini: "     + nrPagini      + "\n" +
                "Disponibil: "    + disponibil    + "\n" +
                "Nr imprumuturi: "+ nrImprumuturi + "\n" +
                "Categorie: "     + categorie     + "\n" +
                "Autori: "        + autori        + "\n" +
                "Rating: "        + rating        + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)                     return true;
        if (!(o instanceof Publicatie p))  return false;
        return id != 0 && id == p.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
