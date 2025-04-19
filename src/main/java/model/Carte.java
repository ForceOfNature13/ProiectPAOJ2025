package model;

import exceptie.MotivIndisponibilitate;
import exceptie.ResursaIndisponibilaExceptie;

import java.util.List;

public class Carte extends Publicatie implements Imprumutabil {

    private String ISBN;
    private Editura editura;

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
    public void afiseazaInfo() {
        System.out.println("Carte:");
        System.out.println("Id: " + this.getId());
        System.out.println("Titlu: " + this.getTitlu());
        System.out.println("An publicare: " + this.getAnPublicare());
        System.out.println("Nr pagini: " + this.getNrPagini());
        System.out.println("Disponibil: " + this.isDisponibil());
        System.out.println("Nr imprumuturi: " + this.getNrImprumuturi());

        System.out.println("Autori: " + this.getAutori());
        System.out.println("ISBN: " + this.getISBN());
        System.out.println("Editura: " + this.getEditura());
        System.out.println("Categorie: " + this.getCategorie());
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

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Editura getEditura() {
        return this.editura;
    }

    public void setEditura(Editura editura) {
        this.editura = editura;
    }

    @Override
    public String toString() {
        return "Carte:\n"
                + super.toString()
                + "ISBN: " + this.getISBN() + "\n"
                + "Editura: " + this.getEditura() + "\n";
    }
}
