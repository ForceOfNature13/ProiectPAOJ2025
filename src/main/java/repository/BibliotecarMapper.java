package repository;

import model.Bibliotecar;
import model.RolBibliotecar;

import java.sql.*;

public class BibliotecarMapper implements RowMapper<Bibliotecar>, StatementBinder<Bibliotecar> {

    @Override
    public Bibliotecar map(ResultSet rs) throws SQLException {
        Bibliotecar b = new Bibliotecar(
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon"),
                rs.getString("username"),
                rs.getString("parola"),
                rs.getString("sectie"),
                rs.getDate("data_angajare").toLocalDate()
        );
        b.setRol(RolBibliotecar.valueOf(rs.getString("rol")));
        b.setBlocat(rs.getBoolean("blocat"));
        return b;
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Bibliotecar b) throws SQLException {
        ps.setString(1, b.getNume());
        ps.setString(2, b.getPrenume());
        ps.setString(3, b.getEmail());
        ps.setString(4, b.getTelefon());
        ps.setString(5, b.getUsername());
        ps.setString(6, b.getParola());
        ps.setBoolean(7, b.getBlocat());
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Bibliotecar b) throws SQLException {
        ps.setString(1, b.getNume());
        ps.setString(2, b.getPrenume());
        ps.setString(3, b.getEmail());
        ps.setString(4, b.getTelefon());
        ps.setString(5, b.getUsername());
        ps.setString(6, b.getParola());
        ps.setBoolean(7, b.getBlocat());
    }

    @Override
    public void insertSecondary(Connection con, Bibliotecar b, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO bibliotecar(id,sectie,data_angajare,rol) VALUES(?,?,?,?)")) {
            ps.setInt(1, id);
            ps.setString(2, b.getSectie());
            ps.setDate(3, Date.valueOf(b.getDataAngajare()));
            ps.setString(4, b.getRol().name());
            ps.executeUpdate();
        }
    }
}
