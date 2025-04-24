package service;

import model.Editura;
import repository.EdituraMapper;
import repository.GenericJdbcRepository;

import java.util.List;
import java.util.Optional;

public class EdituraServiceCrud {

    private static final GenericJdbcRepository<Editura,Integer> repo =
            new GenericJdbcRepository<>(
                    "editura",
                    "id",
                    "INSERT INTO editura(nume,tara) VALUES(?,?)",
                    "UPDATE editura SET nume=?, tara=? WHERE id=?",
                    new EdituraMapper(),
                    new EdituraMapper()
            );

    private EdituraServiceCrud() { }
    private static final EdituraServiceCrud INSTANCE = new EdituraServiceCrud();
    public static EdituraServiceCrud getInstance() { return INSTANCE; }

    public int create(Editura e)              { return repo.save(e); }
    public Optional<Editura> read(int id)     { return repo.findById(id); }
    public List<Editura> readAll()            { return repo.findAll(); }
    public void update(Editura e)             { repo.update(e, e.id()); }
    public void delete(int id)                { repo.deleteById(id); }
}
