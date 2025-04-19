package service;

import exceptie.AccesInterzisExceptie;
import exceptie.EntitateInexistentaExceptie;
import exceptie.LimitaDepasitaExceptie;
import model.Bibliotecar;
import model.Cititor;
import model.Eveniment;
import model.RolBibliotecar;

import java.util.*;

public class EvenimentService {
    private static EvenimentService instance;
    private final Map<Integer, Eveniment> evenimente;

    private EvenimentService() {
        this.evenimente = new HashMap<>();
    }

    public static EvenimentService getInstance() {
        if (instance == null) {
            instance = new EvenimentService();
        }
        return instance;
    }

    public void creazaEveniment(Eveniment e) throws AccesInterzisExceptie {
        var userCurent = AuthService.getInstance().getUtilizatorCurent();
        if (userCurent instanceof Bibliotecar b && b.getRol() == RolBibliotecar.ADMIN) {
            evenimente.put(e.getId(), e);
        } else {
            throw new AccesInterzisExceptie();
        }
    }

    public void stergeEveniment(int idEveniment)
            throws AccesInterzisExceptie, EntitateInexistentaExceptie {

        var userCurent = AuthService.getInstance().getUtilizatorCurent();
        if (!(userCurent instanceof Bibliotecar b) || b.getRol() != RolBibliotecar.ADMIN) {
            throw new AccesInterzisExceptie();
        }

        if (evenimente.remove(idEveniment) == null) {
            throw new EntitateInexistentaExceptie("Eveniment", idEveniment);
        }
    }


    public List<Eveniment> listareEvenimente() {
        return new ArrayList<>(evenimente.values());
    }

    public Eveniment getEvenimentById(int idEveniment) {
        return evenimente.get(idEveniment);
    }

    public void inscrieParticipant(int idEveniment, Cititor c)
            throws EntitateInexistentaExceptie, LimitaDepasitaExceptie {
        Eveniment e = evenimente.get(idEveniment);
        if (e == null) {
            throw new EntitateInexistentaExceptie("Eveniment", idEveniment);
        }
        e.inscrieParticipant(c);
    }

    public void dezinscrieParticipant(int idEveniment, Cititor c)
            throws EntitateInexistentaExceptie {
        Eveniment e = evenimente.get(idEveniment);
        if (e == null) {
            throw new EntitateInexistentaExceptie("Eveniment", idEveniment);
        }
        e.stergeParticipant(c);
    }
}
