package service;

import exceptie.AccesInterzisExceptie;
import exceptie.EntitateInexistentaExceptie;
import exceptie.LimitaDepasitaExceptie;
import model.*;
import observer.EventBus;
import audit.AuditAction;

import java.util.*;

public class EvenimentService {

    private static EvenimentService instance;
    private final Map<Integer, Eveniment> cache = new HashMap<>();

    private EvenimentService() { }

    public static EvenimentService getInstance() {
        if (instance == null) instance = new EvenimentService();
        return instance;
    }

    public void preloadCache() {
        cache.clear();
        EvenimentServiceCrud.getInstance()
                .readAll()
                .forEach(e -> cache.put(e.getId(), e));
    }

    public void creazaEveniment(Eveniment e) throws AccesInterzisExceptie {
        var u = AuthService.getInstance().getUtilizatorCurent();
        if (!(u instanceof Bibliotecar b) || b.getRol() != RolBibliotecar.ADMIN)
            throw new AccesInterzisExceptie();

        EvenimentServiceCrud.getInstance().create(e);
        cache.put(e.getId(), e);
        EventBus.publish(AuditAction.EVENIMENT_CREAT);
    }

    public void stergeEveniment(int id) throws AccesInterzisExceptie, EntitateInexistentaExceptie {
        var u = AuthService.getInstance().getUtilizatorCurent();
        if (!(u instanceof Bibliotecar b) || b.getRol() != RolBibliotecar.ADMIN)
            throw new AccesInterzisExceptie();

        Eveniment ev = cache.get(id);
        if (ev == null) {
            EvenimentServiceCrud.getInstance()
                    .read(id)
                    .orElseThrow(() -> new EntitateInexistentaExceptie("Eveniment", id));
        }

        EvenimentServiceCrud.getInstance().delete(id);
        cache.remove(id);
        EventBus.publish(AuditAction.EVENIMENT_STERS);
    }

    public List<Eveniment> listareEvenimente() {
        List<Eveniment> list = EvenimentServiceCrud.getInstance().readAll();
        cache.clear();
        list.forEach(e -> cache.put(e.getId(), e));
        EventBus.publish(AuditAction.LISTARE_EVENIMENTE);
        return list;
    }

    public void inscrieParticipant(int idEv, Cititor c)
            throws EntitateInexistentaExceptie, LimitaDepasitaExceptie {

        Eveniment e = cache.getOrDefault(idEv,
                EvenimentServiceCrud.getInstance()
                        .read(idEv)
                        .orElseThrow(() ->
                                new EntitateInexistentaExceptie("Eveniment", idEv)));

        e.inscrieParticipant(c);

        EvenimentParticipantServiceCrud.getInstance()
                .create(new EvenimentParticipant(idEv, c.getId()));

        EvenimentServiceCrud.getInstance().update(e);
        cache.put(idEv, e);

        EventBus.publish(AuditAction.INSCRIERE_EVENIMENT);
    }

    public Eveniment readAll() {
        List<Eveniment> list = EvenimentServiceCrud.getInstance().readAll();
        cache.clear();
        list.forEach(e -> cache.put(e.getId(), e));
        EventBus.publish(AuditAction.LISTARE_EVENIMENTE);
        return (Eveniment) list;
    }
}
