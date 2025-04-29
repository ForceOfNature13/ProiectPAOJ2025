package service;

import exceptie.AccesInterzisExceptie;
import exceptie.EntitateInexistentaExceptie;
import exceptie.LimitaDepasitaExceptie;
import model.*;
import observer.EventBus;
import audit.AuditAction;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

        Eveniment ev = cache.computeIfAbsent(idEv, k ->
        {
            try {
                return EvenimentServiceCrud.getInstance()
                        .read(k)
                        .orElseThrow(() ->
                                new EntitateInexistentaExceptie("Eveniment", k));
            } catch (EntitateInexistentaExceptie e) {
                throw new RuntimeException(e);
            }
        });

        ev.inscrieParticipant(c);

        try (Connection conn = ConnectionManager.get().open()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO eveniment_participant(eveniment_id, cititor_id) VALUES (?,?)")) {
                ins.setInt(1, idEv);
                ins.setInt(2, c.getId());
                ins.executeUpdate();
            }

            try (PreparedStatement upd = conn.prepareStatement(
                    "UPDATE eveniment SET nr_participanti=? WHERE id=?")) {
                upd.setInt(1, ev.getNrParticipanti());
                upd.setInt(2, idEv);
                upd.executeUpdate();
            }

            conn.commit();
            cache.put(idEv, ev);

        } catch (Exception ex) {
            throw new RuntimeException("Inscriere esuata, tranzactia a fost anulata", ex);
        }

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
