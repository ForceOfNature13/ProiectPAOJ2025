package util;

import exceptie.LimitaDepasitaExceptie;
import exceptie.TipLimita;
import model.Cititor;
import model.Publicatie;

public class LimitaImprumuturiHandler implements ImprumutValidationHandler {
    private ImprumutValidationHandler next;

    @Override public void setNext(ImprumutValidationHandler n){ next = n; }

    @Override
    public void validate(Publicatie p, Cititor c) throws Exception {
        int active = c.getListaImprumuturiActive().size();
        if (active >= c.getNrMaxImprumuturi())
            throw new LimitaDepasitaExceptie(
                    TipLimita.IMPRUMUTURI_ACTIVE, active, c.getNrMaxImprumuturi());
        if (next != null) next.validate(p, c);
    }
}
