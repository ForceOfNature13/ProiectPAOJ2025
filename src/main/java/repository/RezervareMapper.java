package repository;

import model.Rezervare;
import java.sql.*;

public class RezervareMapper
        implements RowMapper<Rezervare>, StatementBinder<Rezervare> {

    @Override
    public Rezervare map(ResultSet rs) throws SQLException {
        return new Rezervare(
                rs.getInt("publicatie_id"),
                rs.getInt("cititor_id"),
                rs.getTimestamp("data_rezervare"),
                rs.getInt("pozitie")
        );
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Rezervare r) throws SQLException {
        ps.setInt      (1, r.publicatieId());
        ps.setInt      (2, r.cititorId());
        ps.setTimestamp(3, r.dataRezervare());
        ps.setInt      (4, r.pozitie());
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Rezervare r) { }
}
