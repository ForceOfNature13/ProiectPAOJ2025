package repository;

import model.Cititor;

import java.sql.*;
import java.util.ArrayList;

public class CititorMapper implements RowMapper<Cititor>, StatementBinder<Cititor> {

    @Override
    public Cititor map(ResultSet rs) throws SQLException {
        Cititor c = new Cititor(
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon"),
                rs.getString("username"),
                rs.getString("parola"),
                rs.getString("adresa"),
                rs.getInt("nr_max_imprumuturi")
        );
        c.setBlocat(rs.getBoolean("blocat"));
        return c;
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Cititor c) throws SQLException {
        ps.setString(1, c.getNume());
        ps.setString(2, c.getPrenume());
        ps.setString(3, c.getEmail());
        ps.setString(4, c.getTelefon());
        ps.setString(5, c.getUsername());
        ps.setString(6, c.getParola());
        ps.setBoolean(7, c.getBlocat());
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Cititor c) throws SQLException {
        ps.setString(1, c.getNume());
        ps.setString(2, c.getPrenume());
        ps.setString(3, c.getEmail());
        ps.setString(4, c.getTelefon());
        ps.setString(5, c.getUsername());
        ps.setString(6, c.getParola());
        ps.setBoolean(7, c.getBlocat());
    }

    @Override
    public void insertSecondary(Connection con, Cititor cit, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO cititor(id,adresa,nr_max_imprumuturi,suma_penalizari) VALUES(?,?,?,?)")) {
            ps.setInt(1, id);
            ps.setString(2, cit.getAdresa());
            ps.setInt(3, cit.getNrMaxImprumuturi());
            ps.setDouble(4, cit.getSumaPenalizari());
            ps.executeUpdate();
        }
    }
}
