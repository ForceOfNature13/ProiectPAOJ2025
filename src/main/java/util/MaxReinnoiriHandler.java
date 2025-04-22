package util;

import exceptie.LimitaDepasitaExceptie;
import exceptie.TipLimita;
import model.Cititor;
import model.Publicatie;

public class MaxReinnoiriHandler implements ImprumutValidationHandler {
    private static final int MAX_REINNOIRI = 2;
    private ImprumutValidationHandler next;

    @Override public void setNext(ImprumutValidationHandler n){ next = n; }

    @Override
    public void validate(Publicatie p, Cititor c) throws Exception {
        var impr = c.gasesteImprumutActiv(p.getId());
        if (impr != null && impr.getNumarReinnoiri() >= MAX_REINNOIRI)
            throw new LimitaDepasitaExceptie(
                    TipLimita.REINNOIRI, impr.getNumarReinnoiri(), MAX_REINNOIRI);
        if (next != null) next.validate(p, c);
    }
}
