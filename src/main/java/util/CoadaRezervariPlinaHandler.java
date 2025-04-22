package util;

import exceptie.LimitaDepasitaExceptie;
import exceptie.TipLimita;
import model.Cititor;
import model.Publicatie;
import model.RezervarePublicatie;
import java.util.Map;

public class CoadaRezervariPlinaHandler implements ImprumutValidationHandler {
    private final Map<Integer, RezervarePublicatie> rezervari;
    private ImprumutValidationHandler next;

    public CoadaRezervariPlinaHandler(Map<Integer, RezervarePublicatie> rezervari) {
        this.rezervari = rezervari;
    }
    @Override public void setNext(ImprumutValidationHandler n){ next = n; }

    @Override
    public void validate(Publicatie p, Cititor c) throws Exception {
        RezervarePublicatie rez = rezervari.get(p.getId());
        if (rez != null &&
                rez.getCoadaAsteptare().size() >= rez.getLimitaMaxCoada())
            throw new LimitaDepasitaExceptie(
                    TipLimita.COADA_REZERVARE,
                    rez.getCoadaAsteptare().size(),
                    rez.getLimitaMaxCoada());
        if (next != null) next.validate(p, c);
    }
}
