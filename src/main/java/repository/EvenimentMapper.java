package repository;

import model.Eveniment;

import java.sql.*;

public class EvenimentMapper implements RowMapper<Eveniment>, StatementBinder<Eveniment> {

    @Override
    public Eveniment map(ResultSet rs) throws SQLException {
        Eveniment e = new Eveniment(
                rs.getString("titlu"),
                rs.getString("descriere"),
                rs.getDate  ("data").toLocalDate(),
                rs.getString("locatie"),
                rs.getInt   ("capacitate_max")
        );

        e.setId(rs.getInt("id"));
        e.setNrParticipanti(rs.getInt("nr_participanti"));

        return e;
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Eveniment ev) throws SQLException {
        ps.setString(1, ev.getTitlu());
        ps.setString(2, ev.getDescriere());
        ps.setDate  (3, Date.valueOf(ev.getData()));
        ps.setString(4, ev.getLocatie());
        ps.setInt   (5, ev.getCapacitateMax());
        ps.setInt   (6, ev.getNrParticipanti());
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Eveniment ev) throws SQLException {
        bindForInsert(ps, ev);
    }
}
