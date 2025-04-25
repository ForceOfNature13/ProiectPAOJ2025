package repository;

import exceptie.InputInvalidExceptie;
import model.Recenzie;
import model.Identifiable;

import java.sql.*;
import java.time.LocalDateTime;

public class RecenzieMapper implements RowMapper<Recenzie>, StatementBinder<Recenzie> {

    @Override
    public Recenzie map(ResultSet rs) throws SQLException {
        try {
            Recenzie r = new Recenzie(
                    rs.getInt("publicatie_id"),
                    rs.getInt("cititor_id"),
                    rs.getInt("rating"),
                    rs.getString("comentariu"),
                    rs.getTimestamp("data").toLocalDateTime()
            );

            ((Identifiable) r).setId(rs.getInt("id"));

            return r;
        } catch (InputInvalidExceptie e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Recenzie r) throws SQLException {
        ps.setInt(1,  r.getIdPublicatie());
        ps.setInt(2,  r.getIdCititor());
        ps.setInt(3,  r.getRating());
        ps.setString(4, r.getComentariu());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Recenzie r) throws SQLException {
        ps.setInt(1,  r.getRating());
        ps.setString(2, r.getComentariu());
        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
    }
}
