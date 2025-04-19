package service;

import exceptie.AccesInterzisExceptie;
import exceptie.AutentificareExceptie;
import exceptie.CodAutentificare;
import model.Bibliotecar;
import model.Cititor;
import model.Persoana;
import model.RolBibliotecar;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static AuthService instance;
    private final Map<String, Persoana> credentialeUtilizatori;
    private Persoana utilizatorCurent;

    private AuthService() {
        this.credentialeUtilizatori = new HashMap<>();
        this.utilizatorCurent = null;
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public boolean login(String username,
                         String parola,
                         Map<Integer, Cititor> cititori,
                         Map<Integer, Bibliotecar> bibliotecari) {

        for (Cititor cit : cititori.values()) {
            if (cit.getUsername().equals(username)) {
                if (cit.getParola().equals(parola)) {
                    utilizatorCurent = cit;
                    return true;
                }
                return false;
            }
        }
        for (Bibliotecar bibl : bibliotecari.values()) {
            if (bibl.getUsername().equals(username)) {
                if (bibl.getParola().equals(parola)) {
                    utilizatorCurent = bibl;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void logout() {
        utilizatorCurent = null;
    }

    public void inregistreazaUtilizator(String username,
                                        String parola,
                                        Persoana persoana)
            throws AutentificareExceptie {

        if (credentialeUtilizatori.containsKey(username)) {
            throw new AutentificareExceptie(CodAutentificare.USER_EXISTENT);
        }
        persoana.setUsername(username);
        persoana.setParola(parola);
        credentialeUtilizatori.put(username, persoana);
    }

    public void autentifica(String username,
                            String parola)
            throws AutentificareExceptie {

        Persoana p = credentialeUtilizatori.get(username);
        if (p == null) {
            throw new AutentificareExceptie(CodAutentificare.USER_INEXISTENT);
        }
        if (!p.getParola().equals(parola)) {
            throw new AutentificareExceptie(CodAutentificare.PAROLA_GRESITA);
        }
        utilizatorCurent = p;
    }

    public void deconectare() {
        utilizatorCurent = null;
    }

    public void verificaBibliotecar() throws AccesInterzisExceptie {
        if (!(utilizatorCurent instanceof Bibliotecar)) {
            throw new AccesInterzisExceptie();
        }
    }

    public void verificaBibliotecar(RolBibliotecar rolDorit) throws AccesInterzisExceptie {
        if (!(utilizatorCurent instanceof Bibliotecar bibl) || bibl.getRol() != rolDorit) {
            throw new AccesInterzisExceptie();
        }
    }

    public void verificaCititor() throws AccesInterzisExceptie {
        if (!(utilizatorCurent instanceof Cititor)) {
            throw new AccesInterzisExceptie();
        }
    }

    public boolean esteBibliotecar() {
        return utilizatorCurent instanceof Bibliotecar;
    }

    public boolean esteCititor() {
        return utilizatorCurent instanceof Cititor;
    }

    public Persoana getUtilizatorCurent() {
        return utilizatorCurent;
    }

    public Map<String, Persoana> getCredentialeUtilizatori() {
        return Collections.unmodifiableMap(credentialeUtilizatori);
    }
}
