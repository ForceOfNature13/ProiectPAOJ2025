package model;

import exceptie.LimitaDepasitaExceptie;
import exceptie.TipLimita;

import java.time.LocalDate;
import java.util.*;

public class Eveniment {

    private static int idGenerator = 0;

    private int id;
    private String titlu;
    private String descriere;
    private LocalDate data;
    private String locatie;
    private int capacitateMaxima;
    private Set<Cititor> participanti = new TreeSet<>(
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

    public void stergeParticipant(Cititor c) {
        participanti.remove(c);
    }

    public Set<Cititor> getParticipanti() {
        return Collections.unmodifiableSet(participanti);
    }

    public int getId() {
        return id;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public int getCapacitateMaxima() {
        return capacitateMaxima;
    }

    public void setCapacitateMaxima(int capacitateMaxima) {
        this.capacitateMaxima = capacitateMaxima;
    }

    public void setParticipanti(Set<Cititor> participanti) {
        this.participanti = participanti;
    }

    @Override
    public String toString() {
        return "Eveniment:\n"
                + "Id: " + id + "\n"
                + "Titlu: " + titlu + "\n"
                + "Descriere: " + descriere + "\n"
                + "Data: " + data + "\n"
                + "Locatie: " + locatie + "\n"
                + "CapacitateMaxima: " + capacitateMaxima + "\n"
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
