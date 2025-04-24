package service;

import model.Bibliotecar;
import repository.BibliotecarMapper;
import repository.GenericJdbcRepository;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class BibliotecarServiceCrud {

    private static final GenericJdbcRepository<Bibliotecar,Integer> repo =
            new GenericJdbcRepository<>(
                    "(SELECT p.*, b.sectie, b.data_angajare, b.rol " +
                            " FROM persoana p JOIN bibliotecar b ON p.id = b.id) t",
                    "t.id",
                    "INSERT INTO persoana(nume,prenume,email,telefon,username,parola,blocat) VALUES(?,?,?,?,?,?,?)",
                    "UPDATE persoana SET nume=?,prenume=?,email=?,telefon=?,username=?,parola=?,blocat=? WHERE id=?",
                    new BibliotecarMapper(),
                    new BibliotecarMapper()
            );

    private BibliotecarServiceCrud() { }

    private static final BibliotecarServiceCrud INSTANCE = new BibliotecarServiceCrud();
    public static BibliotecarServiceCrud getInstance() { return INSTANCE; }

    public int create(Bibliotecar b)                { return repo.save(b); }
    public Optional<Bibliotecar> read(int id)       { return repo.findById(id); }
    public List<Bibliotecar> readAll()              { return repo.findAll(); }
    public void update(Bibliotecar b)               { repo.update(b, b.getId()); }

    public void delete(int id) {
        try (Connection con = ConnectionManager.get().open()) {
            try (PreparedStatement ps1 = con.prepareStatement("DELETE FROM bibliotecar WHERE id=?");
                 PreparedStatement ps2 = con.prepareStatement("DELETE FROM persoana    WHERE id=?")) {
                con.setAutoCommit(false);
                ps1.setInt(1, id);
                ps1.executeUpdate();
                ps2.setInt(1, id);
                ps2.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new RuntimeException("Eroare delete bibliotecar: " + e.getMessage(), e);
            } finally { try { con.setAutoCommit(true); } catch (Exception ignore) { } }
        } catch (Exception ex) {
            throw new RuntimeException("Eroare delete bibliotecar: " + ex.getMessage(), ex);
        }
    }
}
