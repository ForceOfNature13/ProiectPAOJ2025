package service;

import model.Cititor;
import repository.CititorMapper;
import repository.GenericJdbcRepository;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class CititorServiceCrud {

    private static final GenericJdbcRepository<Cititor,Integer> repo =
            new GenericJdbcRepository<>(
                    "(SELECT p.*, c.adresa, c.nr_max_imprumuturi, c.suma_penalizari " +
                            " FROM persoana p JOIN cititor c ON p.id = c.id) t",
                    "t.id",
                    "INSERT INTO persoana(nume,prenume,email,telefon,username,parola,blocat) VALUES(?,?,?,?,?,?,?)",
                    "UPDATE persoana SET nume=?,prenume=?,email=?,telefon=?,username=?,parola=?,blocat=? WHERE id=?",
                    new CititorMapper(),
                    new CititorMapper()
            );

    private CititorServiceCrud() { }

    private static final CititorServiceCrud INSTANCE = new CititorServiceCrud();
    public static CititorServiceCrud getInstance() { return INSTANCE; }

    public int create(Cititor c)               { return repo.save(c); }
    public Optional<Cititor> read(int id)      { return repo.findById(id); }
    public List<Cititor> readAll()             { return repo.findAll(); }
    public void update(Cititor c)              { repo.update(c, c.getId()); }

    public void delete(int id) {
        try (Connection con = ConnectionManager.get().open()) {
            try (PreparedStatement ps1 = con.prepareStatement("DELETE FROM cititor WHERE id=?");
                 PreparedStatement ps2 = con.prepareStatement("DELETE FROM persoana WHERE id=?")) {
                con.setAutoCommit(false);
                ps1.setInt(1, id);
                ps1.executeUpdate();
                ps2.setInt(1, id);
                ps2.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw new RuntimeException("Eroare delete cititor: " + e.getMessage(), e);
            } finally { try { con.setAutoCommit(true); } catch (Exception ignore) { } }
        } catch (Exception ex) {
            throw new RuntimeException("Eroare delete cititor: " + ex.getMessage(), ex);
        }
    }
}
