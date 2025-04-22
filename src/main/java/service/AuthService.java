package service;

import exceptie.AccesInterzisExceptie;
import exceptie.AutentificareExceptie;
import exceptie.CodAutentificare;
import model.Bibliotecar;
import model.Cititor;
import model.Persoana;
import model.RolBibliotecar;

import observer.EventBus;
import audit.AuditAction;

import java.util.Map;

public class AuthService {

    private static AuthService instance;
    private Persoana utilizatorCurent;

    private AuthService() { this.utilizatorCurent = null; }

    public static AuthService getInstance() {
        if (instance == null) instance = new AuthService();
        return instance;
    }

    public void login(String username,
                      String parola,
                      Map<Integer, Cititor> cititori,
                      Map<Integer, Bibliotecar> bibliotecari)
            throws AutentificareExceptie {

        for (Cititor c : cititori.values()) {
            if (c.getUsername().equals(username)) {
                if (c.getBlocat())
                    throw new AutentificareExceptie(CodAutentificare.CONT_BLOCAT);
                if (!c.getParola().equals(parola))
                    throw new AutentificareExceptie(CodAutentificare.PAROLA_GRESITA);
                utilizatorCurent = c;
                EventBus.publish(AuditAction.AUTENTIFICARE_REUSITA);
                return;
            }
        }

        for (Bibliotecar b : bibliotecari.values()) {
            if (b.getUsername().equals(username)) {
                if (b.getBlocat())
                    throw new AutentificareExceptie(CodAutentificare.CONT_BLOCAT);
                if (!b.getParola().equals(parola))
                    throw new AutentificareExceptie(CodAutentificare.PAROLA_GRESITA);
                utilizatorCurent = b;
                EventBus.publish(AuditAction.AUTENTIFICARE_REUSITA);
                return;
            }
        }

        throw new AutentificareExceptie(CodAutentificare.USER_INEXISTENT);
    }

    public void logout() {
        if (utilizatorCurent == null) return;
        utilizatorCurent = null;
        EventBus.publish(AuditAction.DELOGARE);
    }

    public void verificaBibliotecar(RolBibliotecar rolDorit)
            throws AccesInterzisExceptie {
        if (!(utilizatorCurent instanceof Bibliotecar bibl) ||
                bibl.getRol() != rolDorit)
            throw new AccesInterzisExceptie();
    }

    public Persoana getUtilizatorCurent() { return utilizatorCurent; }
}
