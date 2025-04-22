package model;

import java.time.LocalDate;
import java.util.Objects;

public class Imprumut {
    private static int idGenerator = 0;

    private int id;
    private final int idPublicatie;
    private final int idCititor;
    private final LocalDate dataImprumut;
    private LocalDate dataScadenta;
    private LocalDate dataReturnare;


    private int numarReinnoiri;


    private double penalitate;

    public Imprumut(int idPublicatie,
                    int idCititor,
                    LocalDate dataImprumut,
                    LocalDate dataScadenta,
                    LocalDate dataReturnare) {
        this.id = ++idGenerator;
        this.idPublicatie = idPublicatie;
        this.idCititor = idCititor;
        this.dataImprumut = dataImprumut;
        this.dataScadenta = dataScadenta;
        this.dataReturnare = dataReturnare;


        this.numarReinnoiri = 0;
        this.penalitate = 0.0;
    }
    public Imprumut(int idPub, int idCit) {
        this.idPublicatie = idPub;
        this.idCititor    = idCit;
        this.dataImprumut = LocalDate.now();
        this.dataScadenta = dataImprumut.plusDays(14);
    }


    public int getId() {
        return this.id;
    }

    public int getIdPublicatie() {
        return this.idPublicatie;
    }

    public LocalDate getDataScadenta() {
        return this.dataScadenta;
    }

    public void setDataScadenta(LocalDate dataScadenta) {
        this.dataScadenta = dataScadenta;
    }

    public LocalDate getDataReturnare() {
        return this.dataReturnare;
    }

    public void setDataReturnare(LocalDate dataReturnare) {
        this.dataReturnare = dataReturnare;
    }

    public int getNumarReinnoiri() {
        return this.numarReinnoiri;
    }

    public void setNumarReinnoiri(int numarReinnoiri) {
        this.numarReinnoiri = numarReinnoiri;
    }

    public void setPenalitate(double penalitate) {
        this.penalitate = penalitate;
    }

    @Override
    public String toString() {
        return "Imprumut:\n" +
                "ID: " + this.id + "\n" +
                "ID Publicatie: " + this.idPublicatie + "\n" +
                "ID Cititor: " + this.idCititor + "\n" +
                "Data imprumut: " + this.dataImprumut + "\n" +
                "Data scadenta: " + this.dataScadenta + "\n" +
                "Data returnare: " + this.dataReturnare + "\n" +
                "Numar reinnoiri: " + this.numarReinnoiri + "\n" +
                "Penalitate: " + this.penalitate + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Imprumut imprumut = (Imprumut) o;
        return this.id == imprumut.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
