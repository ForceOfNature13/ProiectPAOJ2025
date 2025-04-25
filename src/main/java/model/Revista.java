package model;

import exceptie.MotivIndisponibilitate;
import exceptie.ResursaIndisponibilaExceptie;

import java.util.List;

public class Revista extends Publicatie {

    private final String frecventaAparitie;
    private final int    numar;

    public Revista(String titlu,
                   int    anPublicare,
                   int    nrPagini,
                   boolean disponibil,
                   List<Recenzie> listaRecenzii,
                   String frecventaAparitie,
                   int    numar,
                   String categorie,
                   List<String> autori) {

        super(titlu, anPublicare, nrPagini, disponibil,
                listaRecenzii, categorie, autori);

        this.frecventaAparitie = frecventaAparitie;
        this.numar             = numar;
    }

    @Override
    public void imprumuta() throws ResursaIndisponibilaExceptie {
        if (!isDisponibil())
            throw new ResursaIndisponibilaExceptie(
                    MotivIndisponibilitate.IMPRUMUTATA, getId());

        setDisponibil(false);
        setNrImprumuturi(getNrImprumuturi() + 1);
    }

    @Override
    public void returneaza() {
        setDisponibil(true);
    }

    @Override
    public int durataMaximaZile() {
        return 7;
    }

    @Override
    public double penalizarePeZi() {
        return 2.0;
    }

    public String getFrecventaAparitie() { return frecventaAparitie; }
    public int    getNumar()             { return numar; }

    @Override
    public String toString() {
        return "Revista:\n"
                + super.toString()
                + "Frecventa aparitie: " + frecventaAparitie + "\n"
                + "Numar: "             + numar             + "\n";
    }
}
