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
import util.ParolaUtil;

import java.util.Optional;

public class AuthService {

    private static AuthService instance;
    private Persoana utilizatorCurent;

    private AuthService() { }

    public static AuthService getInstance() {
        if (instance == null) instance = new AuthService();
        return instance;
    }

    private Optional<Cititor> findCititor(String username) {
        return CititorServiceCrud.getInstance()
                .readAll()
                .stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst();
    }

    private Optional<Bibliotecar> findBibliotecar(String username) {
        return BibliotecarServiceCrud.getInstance()
                .readAll()
                .stream()
                .filter(b -> b.getUsername().equals(username))
                .findFirst();
    }

    public void login(String username, String parola) throws AutentificareExceptie {

        Optional<Cititor> oc = findCititor(username);
        if (oc.isPresent()) {
            Cititor c = oc.get();
            if (c.getBlocat())               throw new AutentificareExceptie(CodAutentificare.CONT_BLOCAT);
            if (!ParolaUtil.verify(parola, c.getParola())) throw new AutentificareExceptie(CodAutentificare.PAROLA_GRESITA);
            utilizatorCurent = c;
            EventBus.publish(AuditAction.AUTENTIFICARE_REUSITA);
            return;
        }

        Optional<Bibliotecar> ob = findBibliotecar(username);
        if (ob.isPresent()) {
            Bibliotecar b = ob.get();
            if (b.getBlocat())               throw new AutentificareExceptie(CodAutentificare.CONT_BLOCAT);
            if (!ParolaUtil.verify(parola, b.getParola())) throw new AutentificareExceptie(CodAutentificare.PAROLA_GRESITA);
            utilizatorCurent = b;
            EventBus.publish(AuditAction.AUTENTIFICARE_REUSITA);
            return;
        }

        throw new AutentificareExceptie(CodAutentificare.USER_INEXISTENT);
    }

    public void logout() {
        if (utilizatorCurent == null) return;
        utilizatorCurent = null;
        EventBus.publish(AuditAction.DELOGARE);
    }

    public void verificaBibliotecar(RolBibliotecar rolDorit) throws AccesInterzisExceptie {
        if (!(utilizatorCurent instanceof Bibliotecar b) || b.getRol() != rolDorit)
            throw new AccesInterzisExceptie();
    }

    public Persoana getUtilizatorCurent() { return utilizatorCurent; }
}
