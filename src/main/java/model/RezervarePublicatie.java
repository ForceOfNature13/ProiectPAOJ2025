package model;
import java.sql.Timestamp;
import exceptie.LimitaDepasitaExceptie;
import exceptie.TipLimita;
import service.RezervareServiceCrud;


import java.util.*;

public class RezervarePublicatie {
    private final int idPublicatie;
    private final Queue<Cititor> coadaAsteptare;
    private final int limitaMaxCoada=2;

    public RezervarePublicatie(int idPublicatie) {
        this.idPublicatie = idPublicatie;
        this.coadaAsteptare = new LinkedList<>();
    }

    public void adaugaInCoada(Cititor cititor) throws LimitaDepasitaExceptie {
        int curent = coadaAsteptare.size();
        if (curent >= limitaMaxCoada)
            throw new LimitaDepasitaExceptie(
                    TipLimita.COADA_REZERVARE, curent, limitaMaxCoada);

        coadaAsteptare.offer(cititor);

        RezervareServiceCrud.getInstance().create(
                new Rezervare(
                        idPublicatie,
                        cititor.getId(),
                        new Timestamp(System.currentTimeMillis()),
                        curent + 1
                )
        );
    }

    public Cititor vizualizeazaUrmatorul() {
        return coadaAsteptare.peek();
    }

    public boolean esteGoala() {
        return coadaAsteptare.isEmpty();
    }
    public List<Cititor> getCoadaAsteptare() {
        return List.copyOf(coadaAsteptare);
    }

    public int getLimitaMaxCoada() {
        return limitaMaxCoada;
    }
    public Cititor veziPrimul() {
        return coadaAsteptare.peek();
    }

    public Cititor extrageUrmatorul() {
        Cititor scos = coadaAsteptare.poll();
        if (scos != null) repoziționeazaPozitii();
        return scos;
    }

    private void repoziționeazaPozitii() {
        int poz = 1;
        for (Cititor c : coadaAsteptare)
            RezervareServiceCrud.getInstance()
                    .updatePosition(idPublicatie, c.getId(), poz++);
    }


    @Override
    public String toString() {
        return "RezervarePublicatie:\n" +
                "IdPublicatie: " + idPublicatie + "\n" +
                "Dimensiune coada: " + coadaAsteptare.size() + "\n" +
                "Limita coada: " + limitaMaxCoada + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RezervarePublicatie that = (RezervarePublicatie) o;
        return idPublicatie == that.idPublicatie;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPublicatie);
    }


}
