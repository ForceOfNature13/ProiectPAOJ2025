package model;

import exceptie.MotivIndisponibilitate;
import exceptie.ResursaIndisponibilaExceptie;

import java.util.List;

public class Audiobook extends Publicatie implements Imprumutabil {

    private final int durata;
    private final List<String> naratori;
    private final String format;

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
    public String toString() {
        return "Audiobook:\n" +
                super.toString() +
                "Durata: " + this.durata + "\n" +
                "Naratori: " + this.naratori + "\n" +
                "Format: " + this.format + "\n";
    }
}
