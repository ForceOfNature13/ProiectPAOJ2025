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
    }

    public abstract void afiseazaInfo();

    public int getId() {
        return this.id;
    }

    public String getTitlu() {
        return this.titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public int getAnPublicare() {
        return this.anPublicare;
    }

    public void setAnPublicare(int anPublicare) {
        this.anPublicare = anPublicare;
    }

    public int getNrPagini() {
        return this.nrPagini;
    }

    public void setNrPagini(int nrPagini) {
        this.nrPagini = nrPagini;
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

    public void setListaRecenzii(List<Recenzie> listaRecenzii) {
        this.listaRecenzii = listaRecenzii;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<String> getAutori() {
        return this.autori;
    }

    public double getRating() {
        if (listaRecenzii == null || listaRecenzii.isEmpty()) {
            return 0.0;
        }
        return listaRecenzii.stream()
                .mapToInt(Recenzie::getRating)
                .average()
                .orElse(0.0);
    }

    public void setAutori(List<String> autori) {
        this.autori = autori;
    }

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
                "Autori: " + this.autori + "\n";
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
