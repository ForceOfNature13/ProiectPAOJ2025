package repository;

import model.Carte;
import model.Editura;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CarteMapper implements RowMapper<Carte>, StatementBinder<Carte> {

    @Override
    public Carte map(ResultSet rs) throws SQLException {
        Editura ed = new Editura(
                rs.getInt   ("editura_id"),
                rs.getString("editura_nume"),
                rs.getString("editura_tara")
        );

        Carte c = new Carte(
                rs.getString ("titlu"),
                rs.getInt    ("an_publicare"),
                rs.getInt    ("nr_pagini"),
                rs.getBoolean("disponibil"),
                new ArrayList<>(),
                Arrays.asList(rs.getString("autori").split(",")),
                rs.getString ("isbn"),
                ed,
                rs.getString ("categorie")
        );

        c.setId(rs.getInt("id"));
        c.setNrImprumuturi(rs.getInt   ("nr_imprumuturi"));
        c.setRating        (rs.getDouble("rating"));

        return c;
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Carte c) throws SQLException {
        ps.setString(1, c.getTitlu());
        ps.setInt   (2, c.getAnPublicare());
        ps.setInt   (3, c.getNrPagini());
        ps.setBoolean(4, c.isDisponibil());
        ps.setInt   (5, c.getNrImprumuturi());
        ps.setString(6, c.getCategorie());
        ps.setDouble(7, c.getRating());
        ps.setString(8, String.join(",", c.getAutori()));
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Carte c) throws SQLException {
        ps.setString(1, c.getTitlu());
        ps.setInt   (2, c.getAnPublicare());
        ps.setInt   (3, c.getNrPagini());
        ps.setBoolean(4, c.isDisponibil());
        ps.setInt   (5, c.getNrImprumuturi());
        ps.setString(6, c.getCategorie());
        ps.setDouble(7, c.getRating());
        ps.setString(8, String.join(",", c.getAutori()));
    }

    @Override
    public void insertSecondary(Connection con, Carte carte, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO carte(id, isbn, editura_id) VALUES (?,?,?)")) {
            ps.setInt   (1, id);
            ps.setString(2, carte.getISBN());
            ps.setInt   (3, carte.getEditura().getId());
            ps.executeUpdate();
        }
    }
}
