package model;

import exceptie.LimitaDepasitaExceptie;
import exceptie.TipLimita;

import java.time.LocalDate;
import java.util.*;

public class Eveniment {

    private static int idGenerator = 0;

    private final int id;
    private final String titlu;
    private final String descriere;
    private final LocalDate data;
    private final String locatie;
    private final int capacitateMaxima;
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
        this.id = ++idGenerator;
        this.titlu = titlu;
        this.descriere = descriere;
        this.data = data;
        this.locatie = locatie;
        this.capacitateMaxima = capacitateMaxima;
    }

    public void inscrieParticipant(Cititor c) throws LimitaDepasitaExceptie {
        int curent = participanti.size();
        if (curent >= capacitateMaxima) {
            throw new LimitaDepasitaExceptie(
                    TipLimita.CAPACITATE_EVENIMENT,
                    curent,
                    capacitateMaxima
            );
        }
        participanti.add(c);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Eveniment:\n"
                + "Id: " + id + "\n"
                + "Titlu: " + titlu + "\n"
                + "Descriere: " + descriere + "\n"
                + "Data: " + data + "\n"
                + "Locatie: " + locatie + "\n"
                + "Capacitate maxima: " + capacitateMaxima + "\n"
                + "Participanti: " + participanti.size() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Eveniment that = (Eveniment) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
