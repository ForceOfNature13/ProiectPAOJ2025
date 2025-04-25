package util;

import exceptie.ResursaIndisponibilaExceptie;
import exceptie.MotivIndisponibilitate;
import model.Cititor;
import model.Publicatie;
import model.RezervarePublicatie;
import java.util.Map;

public class RezervareConflictHandler implements ImprumutValidationHandler {
    private final Map<Integer, RezervarePublicatie> rezervari;
    private ImprumutValidationHandler next;

    public RezervareConflictHandler(Map<Integer, RezervarePublicatie> rezervari) {
        this.rezervari = rezervari;
    }
    @Override public void setNext(ImprumutValidationHandler n){ next = n; }

    @Override
    public void validate(Publicatie p, Cititor c) throws Exception {
        RezervarePublicatie rez = rezervari.get(p.getId());
        if (rez != null && !rez.esteGoala()) {
            var primul = rez.vizualizeazaUrmatorul();
            if (primul != null && !primul.getId().equals(c.getId()))
                throw new ResursaIndisponibilaExceptie(
                        MotivIndisponibilitate.REZERVATA, p.getId());
        }
        if (next != null) next.validate(p, c);
    }
}
