package model;

import exceptie.MotivIndisponibilitate;
import exceptie.ResursaIndisponibilaExceptie;

import java.util.List;

public class Audiobook extends Publicatie implements Imprumutabil {

    private int durata;
    private List<String> naratori;
    private String format;

    public Audiobook(String titlu,
                     int anPublicare,
                     int nrPagini,
                     boolean disponibil,
                     List<Recenzie> listaRecenzii,
                     String categorie,
                     List<String> autori,
                     int durata,
                     List<String> naratori,
                     String format) {

        super(titlu, anPublicare, nrPagini, disponibil, listaRecenzii, categorie, autori);
        this.durata = durata;
        this.naratori = naratori;
        this.format = format;
    }

    @Override
    public void afiseazaInfo() {
        System.out.println("Audiobook:");
        System.out.println("Id: " + this.getId());
        System.out.println("Titlu: " + this.getTitlu());
        System.out.println("An publicare: " + this.getAnPublicare());
        System.out.println("Nr pagini: " + this.getNrPagini());
        System.out.println("Disponibil: " + this.isDisponibil());
        System.out.println("Nr imprumuturi: " + this.getNrImprumuturi());
        System.out.println("Autori (din Publicatie): " + this.getAutori());
        System.out.println("Naratori: " + this.getNaratori());
        System.out.println("Durata: " + this.durata);
        System.out.println("Format: " + this.format);
    }

    @Override
    public void imprumuta() throws ResursaIndisponibilaExceptie {
        if (!this.isDisponibil()) {
            throw new ResursaIndisponibilaExceptie(
                    MotivIndisponibilitate.IMPRUMUTATA,
                    this.getId()
            );
        }
        this.setDisponibil(false);
        this.setNrImprumuturi(this.getNrImprumuturi() + 1);
    }

    @Override
    public void returneaza() {
        this.setDisponibil(true);
    }

    @Override
    public int durataMaximaZile() {
        return 7;
    }

    // Getteri si setteri

    public int getDurata() {
        return this.durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public List<String> getNaratori() {
        return this.naratori;
    }

    public void setNaratori(List<String> naratori) {
        this.naratori = naratori;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "Audiobook:\n" +
                super.toString() +
                "Durata: " + this.durata + "\n" +
                "Naratori: " + this.naratori + "\n" +
                "Format: " + this.format + "\n";
    }
}
