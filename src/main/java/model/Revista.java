package model;

import exceptie.MotivIndisponibilitate;
import exceptie.ResursaIndisponibilaExceptie;

import java.util.List;

public class Revista extends Publicatie implements Imprumutabil {
    private final String frecventaAparitie;
    private final int numar;

    public Revista(String titlu,
                   int anPublicare,
                   int nrPagini,
                   boolean disponibil,
                   List<Recenzie> listaRecenzii,
                   String frecventaAparitie,
                   int numar,
                   String categorie,
                   List<String> autori) {

        super(titlu, anPublicare, nrPagini, disponibil, listaRecenzii, categorie, autori);
        this.frecventaAparitie = frecventaAparitie;
        this.numar = numar;
    }
    public Revista(String titlu, int anPublicare, int nrPagini, boolean disponibil,
                   String frecventaAparitie, int numar,
                   String categorie, List<String> autori) {
        this(titlu,
                anPublicare,
                nrPagini,
                disponibil,
                java.util.Collections.<Recenzie>emptyList(),
                frecventaAparitie,
                numar,
                categorie,
                autori);
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

    @Override
    public double penalizarePeZi() { return 2.0; }

    public String getFrecventaAparitie() {
        return this.frecventaAparitie;
    }

    public int getNumar() {
        return this.numar;
    }

    @Override
    public String toString() {
        return "Revista:\n"
                + super.toString()
                + "Frecventa aparitie: " + this.getFrecventaAparitie() + "\n"
                + "Numar: " + this.getNumar() + "\n";
    }

}
