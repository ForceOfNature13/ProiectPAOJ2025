package service;

import model.Audiobook;
import repository.AudiobookMapper;
import repository.GenericJdbcRepository;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class AudiobookServiceCrud {

    private static final GenericJdbcRepository<Audiobook,Integer> repo =
            new GenericJdbcRepository<>(
                    "(SELECT p.*, a.durata, a.format       " +
                            " FROM publicatie p                     " +
                            " JOIN audiobook a ON p.id = a.id) t",
                    "t.id",
                    "INSERT INTO publicatie(titlu,an_publicare,nr_pagini,disponibil," +
                            "nr_imprumuturi,categorie,rating,autori) VALUES(?,?,?,?,?,?,?,?)",
                    "UPDATE publicatie SET titlu=?,an_publicare=?,nr_pagini=?,disponibil=?," +
                            "nr_imprumuturi=?,categorie=?,rating=?,autori=? WHERE id=?",
                    new AudiobookMapper(),
                    new AudiobookMapper()
            );

    private AudiobookServiceCrud() { }

    private static final AudiobookServiceCrud INSTANCE = new AudiobookServiceCrud();
    public static AudiobookServiceCrud getInstance() { return INSTANCE; }

    public int create(Audiobook a)               { return repo.save(a); }
    public Optional<Audiobook> read(int id)      { return repo.findById(id); }
    public List<Audiobook> readAll()             { return repo.findAll(); }
    public void update(Audiobook a)              { repo.update(a, a.getId()); }

    public void delete(int id) {
        try (Connection con = ConnectionManager.get().open()) {
            try (PreparedStatement ps1 = con.prepareStatement("DELETE FROM audiobook WHERE id=?");
                 PreparedStatement ps2 = con.prepareStatement("DELETE FROM publicatie WHERE id=?")) {
                con.setAutoCommit(false);
                ps1.setInt(1, id);
                ps1.executeUpdate();
                ps2.setInt(1, id);
                ps2.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new RuntimeException("Eroare delete audiobook: " + e.getMessage(), e);
            } finally { try { con.setAutoCommit(true); } catch (Exception ignore) { } }
        } catch (Exception ex) {
            throw new RuntimeException("Eroare delete audiobook: " + ex.getMessage(), ex);
        }
    }
}
