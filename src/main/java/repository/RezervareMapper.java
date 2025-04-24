package repository;

import model.RezervarePublicatie;

import java.sql.*;

public class RezervareMapper implements RowMapper<RezervarePublicatie>, StatementBinder<RezervarePublicatie> {

    @Override
    public RezervarePublicatie map(ResultSet rs) throws SQLException {
        // coada din model o ignori la mapare: scopul e doar sa reconstruim cheile
        return new RezervarePublicatie(rs.getInt("publicatie_id"));
    }

    @Override
    public void bindForInsert(PreparedStatement ps, RezervarePublicatie r) throws SQLException {
        ps.setInt(1, r.getIdPublicatie());
        ps.setInt(2, r.getIdCititor());
    }
    @Override
    public void bindForUpdate(PreparedStatement ps, RezervarePublicatie r) { }
}
