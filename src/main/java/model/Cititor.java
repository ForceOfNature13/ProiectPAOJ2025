package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cititor extends Persoana {
    private List<Imprumut> listaImprumuturiActive;
    private List<Imprumut> istoric;
    private String adresa;
    private double sumaPenalizari;
    private int nrMaxImprumuturi;

    public Cititor(String nume,
                   String prenume,
                   String email,
                   String telefon,
                   String username,
                   String parola,
                   String adresa,
                   int nrMaxImprumuturi) {
        super(nume, prenume, email, telefon, username, parola);

        if (adresa == null || adresa.isBlank()) {
            throw new IllegalArgumentException("Adresa nu poate fi goala.");
        }
        this.adresa = adresa;

        this.listaImprumuturiActive = new ArrayList<>();
        this.istoric = new ArrayList<>();
        this.sumaPenalizari = 0.0;
        this.nrMaxImprumuturi = nrMaxImprumuturi;
    }

    public void adaugaPenalizare(double penalizare) {
        if (penalizare < 0) {
            throw new IllegalArgumentException("Penalizarea nu poate fi negativa.");
        }
        this.sumaPenalizari += penalizare;
    }

    public void platestePenalizare(double suma) {
        if (suma <= 0) {
            throw new IllegalArgumentException("Suma de plata trebuie sa fie pozitiva.");
        }
        this.sumaPenalizari -= suma;
        if (this.sumaPenalizari < 0) {
            this.sumaPenalizari = 0;
        }
    }

    public double getSumaPenalizari() {
        return this.sumaPenalizari;
    }


    public List<Imprumut> getListaImprumuturiActive() {
        return Collections.unmodifiableList(this.listaImprumuturiActive);
    }

    public Imprumut gasesteImprumutActiv(int idPub) {
        return listaImprumuturiActive.stream()
                .filter(i -> i.getIdPublicatie() == idPub && i.getDataReturnare() == null)
                .findFirst().orElse(null);
    }


    public void adaugaImprumutActiv(Imprumut imprumut) {
        this.listaImprumuturiActive.add(imprumut);
    }

    public void stergeImprumutActiv(Imprumut imprumut) {
        this.listaImprumuturiActive.remove(imprumut);
    }


    public List<Imprumut> getIstoric() {
        return Collections.unmodifiableList(this.istoric);
    }

    public void setIstoric(List<Imprumut> istoric) {
        this.istoric = istoric;
    }

    // adresa
    public String getAdresa() {
        return this.adresa;
    }

    public void setAdresa(String adresa) {
        if (adresa == null || adresa.isBlank()) {
            throw new IllegalArgumentException("Adresa nu poate fi goala.");
        }
        this.adresa = adresa;
    }

    public int getNrMaxImprumuturi() {
        return this.nrMaxImprumuturi;
    }

    public void setNrMaxImprumuturi(int nrMaxImprumuturi) {
        this.nrMaxImprumuturi = nrMaxImprumuturi;
    }

    public void adaugaInIstoric(Imprumut imprumut) {
        this.istoric.add(imprumut);
    }


    @Override
    public String toString() {
        return "Cititor:\n"
                + super.toString()
                + "Adresa: " + this.adresa + "\n"
                + "Numar maxim imprumuturi: " + this.nrMaxImprumuturi + "\n"
                + "Penalizari: " + this.sumaPenalizari + "\n";
    }
}
