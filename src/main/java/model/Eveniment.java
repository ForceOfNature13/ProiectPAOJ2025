package model;

import exceptie.LimitaDepasitaExceptie;
import exceptie.TipLimita;

import java.time.LocalDate;
import java.util.*;

public class Eveniment implements Identifiable {

    private int id;
    private final String titlu;
    private final String descriere;
    private final LocalDate data;
    private final String locatie;
    private final int capacitateMaxima;

    private int nrParticipanti;

    private final Set<Cititor> participanti = new TreeSet<>(
            Comparator.comparing(Cititor::getNume)
                    .thenComparing(Cititor::getPrenume)
                    .thenComparingInt(Cititor::getId)
    );

    public Eveniment(String titlu,
                     String descriere,
                     LocalDate data,
                     String locatie,
                     int capacitateMaxima) {

        this.id               = 0;
        this.titlu            = titlu;
        this.descriere        = descriere;
        this.data             = data;
        this.locatie          = locatie;
        this.capacitateMaxima = capacitateMaxima;

        this.nrParticipanti   = 0;
    }

    /*–––– înscriere participant ––––*/
    public void inscrieParticipant(Cititor c) throws LimitaDepasitaExceptie {
        if (nrParticipanti >= capacitateMaxima) {
            throw new LimitaDepasitaExceptie(
                    TipLimita.CAPACITATE_EVENIMENT,
                    nrParticipanti,
                    capacitateMaxima);
        }
        participanti.add(c);
        nrParticipanti++;                // ↑ contorul
    }

    /*–––– GET / SET ––––*/
    @Override public Integer getId()      { return id; }
    @Override public void setId(Integer id){ this.id = id; }

    public String    getTitlu()         { return titlu; }
    public LocalDate getData()          { return data; }
    public String    getLocatie()       { return locatie; }
    public int       getCapacitateMax() { return capacitateMaxima; }

    /** număr participanţi salvat în DB */
    public int  getNrParticipanti()     { return nrParticipanti; }
    public void setNrParticipanti(int n){ this.nrParticipanti = n; }

    /*–––– toString ––––*/
    @Override
    public String toString() {
        return "Eveniment:\n" +
                "Id: "                 + id               + "\n" +
                "Titlu: "              + titlu            + "\n" +
                "Descriere: "          + descriere        + "\n" +
                "Data: "               + data             + "\n" +
                "Locatie: "            + locatie          + "\n" +
                "Capacitate maxima: "  + capacitateMaxima + "\n" +
                "Participanti: "       + nrParticipanti   + "\n";
    }

    /*–––– egalitate / hash ––––*/
    @Override
    public boolean equals(Object o) {
        if (this == o)               return true;
        if (!(o instanceof Eveniment e)) return false;
        return id != 0 && id == e.id;
    }
    @Override
    public int hashCode() { return Objects.hash(id); }

    public String getDescriere() { return descriere; }
}
