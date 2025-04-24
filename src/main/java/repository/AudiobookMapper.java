package repository;

import model.Audiobook;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;

public class AudiobookMapper implements RowMapper<Audiobook>, StatementBinder<Audiobook> {

    @Override
    public Audiobook map(ResultSet rs) throws SQLException {
        return new Audiobook(
                rs.getString("titlu"),
                rs.getInt("an_publicare"),
                rs.getInt("nr_pagini"),
                rs.getBoolean("disponibil"),
                Collections.emptyList(),
                rs.getString("categorie"),
                Arrays.asList(rs.getString("autori").split(",")),
                rs.getInt("durata"),
                Collections.emptyList(),
                rs.getString("format")
        );
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Audiobook a) throws SQLException {
        ps.setString(1, a.getTitlu());
        ps.setInt(2, a.getAnPublicare());
        ps.setInt(3, a.getNrPagini());
        ps.setBoolean(4, a.isDisponibil());
        ps.setInt(5, a.getNrImprumuturi());
        ps.setString(6, a.getCategorie());
        ps.setDouble(7, a.getRating());
        ps.setString(8, String.join(",", a.getAutori()));
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Audiobook a) throws SQLException {
        ps.setString(1, a.getTitlu());
        ps.setInt(2, a.getAnPublicare());
        ps.setInt(3, a.getNrPagini());
        ps.setBoolean(4, a.isDisponibil());
        ps.setInt(5, a.getNrImprumuturi());
        ps.setString(6, a.getCategorie());
        ps.setDouble(7, a.getRating());
        ps.setString(8, String.join(",", a.getAutori()));
    }

    @Override
    public void insertSecondary(Connection c, Audiobook a, int id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO audiobook(id,durata,format) VALUES(?,?,?)")) {
            ps.setInt(1, id);
            ps.setInt(2, a.getDurata());
            ps.setString(3, a.getFormat());
            ps.executeUpdate();
        }
    }
}
