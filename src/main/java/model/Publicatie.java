package model;

import java.util.List;
import java.util.Objects;

public abstract class Publicatie implements Imprumutabil{
    private static int idGenerator = 0;

    protected int id;
    protected String titlu;
    protected int anPublicare;
    protected int nrPagini;
    protected boolean disponibil;
    protected int nrImprumuturi;
    protected List<Recenzie> listaRecenzii;
    protected String categorie;
    protected double rating;

    protected List<String> autori;


    public Publicatie(String titlu, int anPublicare, int nrPagini, boolean disponibil,
                      List<Recenzie> listaRecenzii, String categorie, List<String> autori) {
        this.id = ++idGenerator;
        this.titlu = titlu;
        this.anPublicare = anPublicare;
        this.nrPagini = nrPagini;
        this.disponibil = disponibil;
        this.nrImprumuturi = 0;
        this.listaRecenzii = listaRecenzii;
        this.categorie = categorie;
        this.autori = autori;
        this.rating= calculeazaRatingIntern();
    }

    public int getId() {
        return this.id;
    }

    public String getTitlu() {
        return this.titlu;
    }

    public int getAnPublicare() {
        return this.anPublicare;
    }

    public boolean isDisponibil() {
        return this.disponibil;
    }

    public void setDisponibil(boolean disponibil) {
        this.disponibil = disponibil;
    }

    public int getNrImprumuturi() {
        return this.nrImprumuturi;
    }

    public void setNrImprumuturi(int nrImprumuturi) {
        this.nrImprumuturi = nrImprumuturi;
    }

    public List<Recenzie> getListaRecenzii() {
        return this.listaRecenzii;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public List<String> getAutori() {
        return this.autori;
    }

    private double calculeazaRatingIntern() {
        if (listaRecenzii == null || listaRecenzii.isEmpty()) return 0;
        return listaRecenzii.stream()
                .mapToInt(Recenzie::getRating)
                .average()
                .orElse(0);
    }

    public double getRating() { return rating; }

    public void updateRating() { this.rating = calculeazaRatingIntern(); }


    @Override
    public String toString() {
        return "Publicatie:\n" +
                "Id: " + this.id + "\n" +
                "Titlu: " + this.titlu + "\n" +
                "An publicare: " + this.anPublicare + "\n" +
                "Nr pagini: " + this.nrPagini + "\n" +
                "Disponibil: " + this.disponibil + "\n" +
                "Nr imprumuturi: " + this.nrImprumuturi + "\n" +
                "Categorie: " + this.categorie + "\n" +
                "Autori: " + this.autori + "\n"+
                "Rating: " + this.rating + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Publicatie that = (Publicatie) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
