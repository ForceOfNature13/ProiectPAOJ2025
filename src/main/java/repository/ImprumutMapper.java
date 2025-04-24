package repository;

import model.Imprumut;

import java.sql.*;
import java.time.LocalDate;

public class ImprumutMapper implements RowMapper<Imprumut>, StatementBinder<Imprumut> {

    @Override
    public Imprumut map(ResultSet rs) throws SQLException {
        Imprumut i = new Imprumut(
                rs.getInt("publicatie_id"),
                rs.getInt("cititor_id"),
                rs.getDate("data_imprumut").toLocalDate(),
                rs.getDate("data_scadenta").toLocalDate(),
                rs.getDate("data_returnare") == null ? null :
                        rs.getDate("data_returnare").toLocalDate()
        );
        i.setNumarReinnoiri(rs.getInt("numar_reinnoiri"));
        i.setPenalitate(rs.getDouble("penalitate"));
        return i;
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Imprumut i) throws SQLException {
        ps.setInt(1,  i.getIdPublicatie());
        ps.setInt(2,  i.getIdCititor());
        ps.setDate(3, Date.valueOf(i.getDataImprumut()));
        ps.setDate(4, Date.valueOf(i.getDataScadenta()));
        if (i.getDataReturnare() != null)
            ps.setDate(5, Date.valueOf(i.getDataReturnare()));
        else
            ps.setNull(5, Types.DATE);
        ps.setInt(6,  i.getNumarReinnoiri());
        ps.setDouble(7, i.getPenalitate());
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Imprumut i) throws SQLException {
        ps.setInt(1,  i.getIdPublicatie());
        ps.setInt(2,  i.getIdCititor());
        ps.setDate(3, Date.valueOf(i.getDataImprumut()));
        ps.setDate(4, Date.valueOf(i.getDataScadenta()));
        if (i.getDataReturnare() != null)
            ps.setDate(5, Date.valueOf(i.getDataReturnare()));
        else
            ps.setNull(5, Types.DATE);
        ps.setInt(6,  i.getNumarReinnoiri());
        ps.setDouble(7, i.getPenalitate());
    }
}
