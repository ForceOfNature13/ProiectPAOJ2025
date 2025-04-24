package service;

import model.Eveniment;
import repository.EvenimentMapper;
import repository.GenericJdbcRepository;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class EvenimentServiceCrud {

    private static final GenericJdbcRepository<Eveniment,Integer> repo =
            new GenericJdbcRepository<>(
                    "eveniment",
                    "id",
                    "INSERT INTO eveniment(titlu,descriere,data,locatie,capacitate_max) VALUES(?,?,?,?,?)",
                    "UPDATE eveniment SET titlu=?,descriere=?,data=?,locatie=?,capacitate_max=? WHERE id=?",
                    new EvenimentMapper(),
                    new EvenimentMapper()
            );

    private EvenimentServiceCrud() { }
    private static final EvenimentServiceCrud I = new EvenimentServiceCrud();
    public static EvenimentServiceCrud getInstance() { return I; }

    public int create(Eveniment e)               { return repo.save(e); }
    public Optional<Eveniment> read(int id)      { return repo.findById(id); }
    public List<Eveniment> readAll()             { return repo.findAll(); }
    public void update(Eveniment e)              { repo.update(e,e.getId()); }
    public void delete(int id)                   { repo.deleteById(id); }
}
