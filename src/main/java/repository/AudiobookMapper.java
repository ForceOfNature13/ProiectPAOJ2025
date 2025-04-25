package repository;

import model.Audiobook;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AudiobookMapper implements RowMapper<Audiobook>, StatementBinder<Audiobook> {

    @Override
    public Audiobook map(ResultSet rs) throws SQLException {
        Audiobook a = new Audiobook(
                rs.getString ("titlu"),
                rs.getInt    ("an_publicare"),
                rs.getInt    ("nr_pagini"),
                rs.getBoolean("disponibil"),
                new ArrayList<>(),
                rs.getString ("categorie"),
                Arrays.asList(rs.getString("autori").split(",")),
                rs.getInt    ("durata"),
                new ArrayList<>(),
                rs.getString ("format")
        );

        a.setId(rs.getInt("id"));
        a.setNrImprumuturi(rs.getInt   ("nr_imprumuturi"));
        a.setRating        (rs.getDouble("rating"));

        return a;
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Audiobook a) throws SQLException {
        ps.setString(1, a.getTitlu());
        ps.setInt   (2, a.getAnPublicare());
        ps.setInt   (3, a.getNrPagini());
        ps.setBoolean(4, a.isDisponibil());
        ps.setInt   (5, a.getNrImprumuturi());
        ps.setString(6, a.getCategorie());
        ps.setDouble(7, a.getRating());
        ps.setString(8, String.join(",", a.getAutori()));
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Audiobook a) throws SQLException {
        ps.setString(1, a.getTitlu());
        ps.setInt   (2, a.getAnPublicare());
        ps.setInt   (3, a.getNrPagini());
        ps.setBoolean(4, a.isDisponibil());
        ps.setInt   (5, a.getNrImprumuturi());
        ps.setString(6, a.getCategorie());
        ps.setDouble(7, a.getRating());
        ps.setString(8, String.join(",", a.getAutori()));
    }

    @Override
    public void insertSecondary(Connection con, Audiobook a, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO audiobook(id, durata, format) VALUES (?,?,?)")) {
            ps.setInt   (1, id);
            ps.setInt   (2, a.getDurata());
            ps.setString(3, a.getFormat());
            ps.executeUpdate();
        }
    }
}
