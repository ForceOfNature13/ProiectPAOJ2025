package service;

import model.Revista;
import repository.GenericJdbcRepository;
import repository.RevistaMapper;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class RevistaServiceCrud {

    private static final GenericJdbcRepository<Revista,Integer> repo =
            new GenericJdbcRepository<>(
                    "(SELECT p.*, r.frecventa, r.numar " +
                            " FROM publicatie p JOIN revista r ON p.id = r.id) t",
                    "t.id",
                    "INSERT INTO publicatie(titlu,an_publicare,nr_pagini,disponibil," +
                            "nr_imprumuturi,categorie,rating,autori) VALUES(?,?,?,?,?,?,?,?)",
                    "UPDATE publicatie SET titlu=?,an_publicare=?,nr_pagini=?,disponibil=?," +
                            "nr_imprumuturi=?,categorie=?,rating=?,autori=? WHERE id=?",
                    new RevistaMapper(),
                    new RevistaMapper()
            );

    private RevistaServiceCrud() { }

    private static final RevistaServiceCrud INSTANCE = new RevistaServiceCrud();
    public static RevistaServiceCrud getInstance() { return INSTANCE; }

    public int create(Revista r)               { return repo.save(r); }
    public Optional<Revista> read(int id)      { return repo.findById(id); }
    public List<Revista> readAll()             { return repo.findAll(); }
    public void update(Revista r)              { repo.update(r, r.getId()); }

    public void delete(int id) {
        try (Connection con = ConnectionManager.get().open()) {
            try (PreparedStatement ps1 = con.prepareStatement("DELETE FROM revista WHERE id=?");
                 PreparedStatement ps2 = con.prepareStatement("DELETE FROM publicatie WHERE id=?")) {
                con.setAutoCommit(false);
                ps1.setInt(1, id);
                ps1.executeUpdate();
                ps2.setInt(1, id);
                ps2.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new RuntimeException("Eroare delete revista: " + e.getMessage(), e);
            } finally { try { con.setAutoCommit(true); } catch (Exception ignore) { } }
        } catch (Exception ex) {
            throw new RuntimeException("Eroare delete revista: " + ex.getMessage(), ex);
        }
    }

}
