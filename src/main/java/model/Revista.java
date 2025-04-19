package model;

import exceptie.MotivIndisponibilitate;
import exceptie.ResursaIndisponibilaExceptie;

import java.util.List;

public class Revista extends Publicatie implements Imprumutabil {
    private String frecventaAparitie;
    private int numar;

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

    @Override
    public void afiseazaInfo() {
        System.out.println("Revista:");
        System.out.println("Id: " + this.getId());
        System.out.println("Titlu: " + this.getTitlu());
        System.out.println("An publicare: " + this.getAnPublicare());
        System.out.println("Nr pagini: " + this.getNrPagini());
        System.out.println("Disponibil: " + this.isDisponibil());
        System.out.println("Nr imprumuturi: " + this.getNrImprumuturi());
        System.out.println("Frecventa aparitie: " + this.getFrecventaAparitie());
        System.out.println("Numar: " + this.getNumar());
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
        return 7;
    }

    public String getFrecventaAparitie() {
        return this.frecventaAparitie;
    }

    public void setFrecventaAparitie(String frecventaAparitie) {
        this.frecventaAparitie = frecventaAparitie;
    }

    public int getNumar() {
        return this.numar;
    }

    public void setNumar(int numar) {
        this.numar = numar;
    }

    @Override
    public String toString() {
        return "Revista:\n"
                + super.toString()
                + "Frecventa aparitie: " + this.getFrecventaAparitie() + "\n"
                + "Numar: " + this.getNumar() + "\n";
    }
}
