package repository;

import exceptie.InputInvalidExceptie;
import model.Recenzie;

import java.sql.*;
import java.time.LocalDateTime;

public class RecenzieMapper implements RowMapper<Recenzie>, StatementBinder<Recenzie> {

    @Override
    public Recenzie map(ResultSet rs) throws SQLException {
        try {
            return new Recenzie(
                    rs.getInt("publicatie_id"),
                    rs.getInt("cititor_id"),
                    rs.getInt("rating"),
                    rs.getString("comentariu"),
                    rs.getTimestamp("data").toLocalDateTime()
            );
        } catch (InputInvalidExceptie e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Recenzie r) throws SQLException {
        ps.setInt(1,  r.getIdPublicatie());
        ps.setInt(2,  r.getIdCititor());
        ps.setInt(3,  r.getRating());
        ps.setString(4, r.toString().contains("Comentariu") ? r.toString() : r.toString()); // string, not ideal but stub
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Recenzie r) throws SQLException {
        ps.setInt(1,  r.getRating());
        ps.setString(2, r.toString().contains("Comentariu") ? r.toString() : r.toString()); // string, not ideal but stub
        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
    }
}
