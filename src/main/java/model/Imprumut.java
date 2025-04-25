package model;

import java.time.LocalDate;
import java.util.Objects;

public class Imprumut implements Identifiable {

    private int id;
    private final int idPublicatie;
    private final int idCititor;
    private final LocalDate dataImprumut;
    private       LocalDate dataScadenta;
    private       LocalDate dataReturnare;

    private int    numarReinnoiri;
    private double penalitate;

    public Imprumut(int idPublicatie,
                    int idCititor,
                    LocalDate dataImprumut,
                    LocalDate dataScadenta,
                    LocalDate dataReturnare) {

        this.id             = 0;
        this.idPublicatie   = idPublicatie;
        this.idCititor      = idCititor;
        this.dataImprumut   = dataImprumut;
        this.dataScadenta   = dataScadenta;
        this.dataReturnare  = dataReturnare;

        this.numarReinnoiri = 0;
        this.penalitate     = 0.0;
    }

    public Imprumut(int idPub, int idCit) {
        this(idPub,
                idCit,
                LocalDate.now(),
                LocalDate.now().plusDays(14),
                null);
    }

    @Override
    public Integer getId()        { return id; }
    @Override
    public void   setId(Integer id) { this.id = id; }

    public int       getIdPublicatie()  { return idPublicatie; }
    public int       getIdCititor()     { return idCititor; }
    public LocalDate getDataImprumut()  { return dataImprumut; }
    public LocalDate getDataScadenta()  { return dataScadenta; }
    public void      setDataScadenta(LocalDate d) { dataScadenta = d; }
    public LocalDate getDataReturnare() { return dataReturnare; }
    public void      setDataReturnare(LocalDate d) { dataReturnare = d; }
    public int       getNumarReinnoiri(){ return numarReinnoiri; }
    public void      setNumarReinnoiri(int n){ numarReinnoiri = n; }
    public double    getPenalitate()    { return penalitate; }
    public void      setPenalitate(double p){ penalitate = p; }

    @Override
    public String toString() {
        return "Imprumut:\n" +
                "ID: "             + id             + "\n" +
                "ID Publicatie: "  + idPublicatie   + "\n" +
                "ID Cititor: "     + idCititor      + "\n" +
                "Data imprumut: "  + dataImprumut   + "\n" +
                "Data scadenta: "  + dataScadenta   + "\n" +
                "Data returnare: " + dataReturnare  + "\n" +
                "Numar reinnoiri: "+ numarReinnoiri + "\n" +
                "Penalitate: "     + penalitate     + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Imprumut imp)) return false;
        return id != 0 && id == imp.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
