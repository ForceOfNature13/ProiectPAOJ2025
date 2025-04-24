package service;

import model.Carte;
import repository.CarteMapper;
import repository.GenericJdbcRepository;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class CarteServiceCrud {

    private static final GenericJdbcRepository<Carte, Integer> repo =
            new GenericJdbcRepository<>(
                    "(SELECT  p.*,                               " +
                            "         c.isbn,                            " +
                            "         c.editura_id,                      " +
                            "         e.nume AS editura_nume,            " +
                            "         e.tara AS editura_tara             " +
                            "  FROM publicatie p                         " +
                            "  JOIN carte   c ON p.id = c.id             " +
                            "  JOIN editura e ON c.editura_id = e.id) t",
                    "t.id",
                    "INSERT INTO publicatie(titlu,an_publicare,nr_pagini,disponibil," +
                            "nr_imprumuturi,categorie,rating,autori) VALUES(?,?,?,?,?,?,?,?)",
                    "UPDATE publicatie SET titlu=?,an_publicare=?,nr_pagini=?,disponibil=?," +
                            "nr_imprumuturi=?,categorie=?,rating=?,autori=? WHERE id=?",
                    new CarteMapper(),
                    new CarteMapper()
            );

    private CarteServiceCrud() { }

    private static final CarteServiceCrud INSTANCE = new CarteServiceCrud();
    public static CarteServiceCrud getInstance() { return INSTANCE; }

    public int create(Carte c)               { return repo.save(c); }
    public Optional<Carte> read(int id)      { return repo.findById(id); }
    public List<Carte> readAll()             { return repo.findAll(); }
    public void update(Carte c)              { repo.update(c, c.getId()); }

    public void delete(int id) {
        try (Connection con = ConnectionManager.get().open()) {
            try (PreparedStatement ps1 = con.prepareStatement("DELETE FROM carte WHERE id=?");
                 PreparedStatement ps2 = con.prepareStatement("DELETE FROM publicatie WHERE id=?")) {
                con.setAutoCommit(false);
                ps1.setInt(1, id);
                ps1.executeUpdate();
                ps2.setInt(1, id);
                ps2.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new RuntimeException("Eroare la delete carte: " + e.getMessage(), e);
            } finally {
                try { con.setAutoCommit(true); } catch (Exception ignore) { }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Eroare la delete carte: " + ex.getMessage(), ex);
        }
    }
}
