package repository;

import model.Revista;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;

public class RevistaMapper implements RowMapper<Revista>, StatementBinder<Revista> {

    @Override
    public Revista map(ResultSet rs) throws SQLException {
        return new Revista(
                rs.getString("titlu"),
                rs.getInt("an_publicare"),
                rs.getInt("nr_pagini"),
                rs.getBoolean("disponibil"),
                Collections.emptyList(),
                rs.getString("frecventa"),
                rs.getInt("numar"),
                rs.getString("categorie"),
                Arrays.asList(rs.getString("autori").split(","))
        );
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Revista r) throws SQLException {
        ps.setString(1, r.getTitlu());
        ps.setInt(2, r.getAnPublicare());
        ps.setInt(3, r.getNrPagini());
        ps.setBoolean(4, r.isDisponibil());
        ps.setInt(5, r.getNrImprumuturi());
        ps.setString(6, r.getCategorie());
        ps.setDouble(7, r.getRating());
        ps.setString(8, String.join(",", r.getAutori()));
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Revista r) throws SQLException {
        ps.setString(1, r.getTitlu());
        ps.setInt(2, r.getAnPublicare());
        ps.setInt(3, r.getNrPagini());
        ps.setBoolean(4, r.isDisponibil());
        ps.setInt(5, r.getNrImprumuturi());
        ps.setString(6, r.getCategorie());
        ps.setDouble(7, r.getRating());
        ps.setString(8, String.join(",", r.getAutori()));
    }

    @Override
    public void insertSecondary(Connection c, Revista rev, int id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO revista(id,frecventa,numar) VALUES(?,?,?)")) {
            ps.setInt(1, id);
            ps.setString(2, rev.getFrecventaAparitie());
            ps.setInt(3, rev.getNumar());
            ps.executeUpdate();
        }
    }
}
