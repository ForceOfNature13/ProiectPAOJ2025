package service;

import model.Imprumut;
import repository.GenericJdbcRepository;
import repository.ImprumutMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ImprumutServiceCrud {

    private static final GenericJdbcRepository<Imprumut,Integer> repo =
            new GenericJdbcRepository<>(
                    "imprumut",
                    "id",
                    "INSERT INTO imprumut(publicatie_id,cititor_id,data_imprumut,data_scadenta," +
                            "data_returnare,numar_reinnoiri,penalitate) VALUES(?,?,?,?,?,?,?)",
                    "UPDATE imprumut SET publicatie_id=?,cititor_id=?,data_imprumut=?,data_scadenta=?," +
                            "data_returnare=?,numar_reinnoiri=?,penalitate=? WHERE id=?",
                    new ImprumutMapper(),
                    new ImprumutMapper()
            );

    private ImprumutServiceCrud() { }

    private static final ImprumutServiceCrud I = new ImprumutServiceCrud();
    public static ImprumutServiceCrud getInstance() { return I; }

    public int create(Imprumut i)               { return repo.save(i); }
    public Optional<Imprumut> read(int id)      { return repo.findById(id); }
    public List<Imprumut> readAll()             { return repo.findAll(); }
    public void update(Imprumut i)              { repo.update(i,i.getId()); }
    public void delete(int id)                  { repo.deleteById(id); }

    public int create(Connection con, Imprumut i) throws SQLException {
        return repo.save(con, i);
    }
    public List<Imprumut> findActiveByCititor(int cititorId) {
        String q = "SELECT * FROM imprumut WHERE cititor_id = ? AND data_returnare IS NULL";
        return repo.findMany(q, cititorId);
    }
    public List<Imprumut> findIstoricReturnatByCititor(int cititorId) {
        String q = "SELECT * FROM imprumut " +
                "WHERE cititor_id = ? AND data_returnare IS NOT NULL";
        return repo.findMany(q, cititorId);
    }

    public List<Imprumut> findPenalizateByCititor(int cititorId) {
        String q = "SELECT * FROM imprumut " +
                "WHERE cititor_id = ? AND penalitate > 0";
        return repo.findMany(q, cititorId);
    }
}
