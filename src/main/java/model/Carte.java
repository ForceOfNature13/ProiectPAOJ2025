package model;

import exceptie.MotivIndisponibilitate;
import exceptie.ResursaIndisponibilaExceptie;

import java.util.List;

public class Carte extends Publicatie implements Imprumutabil {

    private final String ISBN;
    private final Editura editura;

    public Carte(String titlu, int anPublicare, int nrPagini, boolean disponibil,
                 List<Recenzie> listaRecenzii,
                 List<String> autori,
                 String ISBN,
                 Editura editura,
                 String categorie) {

        super(titlu, anPublicare, nrPagini, disponibil, listaRecenzii, categorie, autori);
        this.ISBN = ISBN;
        this.editura = editura;
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
        return 14;
    }

    public String getISBN() {
        return this.ISBN;
    }

    public Editura getEditura() {
        return this.editura;
    }

    @Override
    public String toString() {
        return "Carte:\n"
                + super.toString()
                + "ISBN: " + this.getISBN() + "\n"
                + "Editura: " + this.getEditura() + "\n";
    }
}
