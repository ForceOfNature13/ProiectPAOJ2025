package service;

import model.Recenzie;
import repository.GenericJdbcRepository;
import repository.RecenzieMapper;

import java.util.List;
import java.util.Optional;

public class RecenzieServiceCrud {

    private static final GenericJdbcRepository<Recenzie,Integer> repo =
            new GenericJdbcRepository<>(
                    "recenzie",
                    "id",
                    "INSERT INTO recenzie(publicatie_id,cititor_id,rating,comentariu,data) VALUES(?,?,?,?,?)",
                    "UPDATE recenzie SET rating=?,comentariu=?,data=? WHERE id=?",
                    new RecenzieMapper(),
                    new RecenzieMapper()
            );

    private RecenzieServiceCrud() { }
    private static final RecenzieServiceCrud I = new RecenzieServiceCrud();
    public static RecenzieServiceCrud getInstance() { return I; }

    public int create(Recenzie r)              { return repo.save(r); }
    public Optional<Recenzie> read(int id)     { return repo.findById(id); }
    public List<Recenzie> readAll()            { return repo.findAll(); }
    public void update(Recenzie r)             { repo.update(r, r.getId()); }
    public void delete(int id)                 { repo.deleteById(id); }

    public List<Recenzie> findByCititor(int cititorId) {
        String q = "SELECT * FROM recenzie WHERE cititor_id = ?";
        return repo.findMany(q, cititorId);
    }
}
