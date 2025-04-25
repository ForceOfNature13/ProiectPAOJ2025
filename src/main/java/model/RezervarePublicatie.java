package model;

import exceptie.LimitaDepasitaExceptie;
import exceptie.TipLimita;

import java.time.LocalDateTime;
import java.util.*;

public class RezervarePublicatie {
    private final int idPublicatie;
    private final Queue<Cititor> coadaAsteptare;
    private final int limitaMaxCoada=5;

    public RezervarePublicatie(int idPublicatie) {
        this.idPublicatie = idPublicatie;
        this.coadaAsteptare = new LinkedList<>();
    }

    public void adaugaInCoada(Cititor cititor) throws LimitaDepasitaExceptie {
        int curent = coadaAsteptare.size();
        if (curent >= limitaMaxCoada) {
            throw new LimitaDepasitaExceptie(
                    TipLimita.COADA_REZERVARE,
                    curent,
                    limitaMaxCoada
            );
        }
        coadaAsteptare.offer(cititor);
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
    public Cititor extrageUrmatorul() {
        return coadaAsteptare.poll();
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

    public int getIdPublicatie() {
        return this.idPublicatie;
    }

    public int getIdCititor() {
        if (coadaAsteptare.isEmpty()) {
            return -1;
        }
        return coadaAsteptare.peek().getId();
    }


}
