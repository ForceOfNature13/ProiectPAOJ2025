package repository;

import model.Carte;
import model.Editura;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;

public class CarteMapper implements RowMapper<Carte>, StatementBinder<Carte> {

    @Override
    public Carte map(ResultSet rs) throws SQLException {
        Editura ed = new Editura(
                rs.getInt("editura_id"),
                rs.getString("editura_nume"),
                rs.getString("editura_tara")
        );
        return new Carte(
                rs.getString("titlu"),
                rs.getInt("an_publicare"),
                rs.getInt("nr_pagini"),
                rs.getBoolean("disponibil"),
                Collections.emptyList(),
                Arrays.asList(rs.getString("autori").split(",")),
                rs.getString("isbn"),
                ed,
                rs.getString("categorie")
        );
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Carte c) throws SQLException {
        ps.setString(1, c.getTitlu());
        ps.setInt(2, c.getAnPublicare());
        ps.setInt(3, c.getNrPagini());
        ps.setBoolean(4, c.isDisponibil());
        ps.setInt(5, c.getNrImprumuturi());
        ps.setString(6, c.getCategorie());
        ps.setDouble(7, c.getRating());
        ps.setString(8, String.join(",", c.getAutori()));
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Carte c) throws SQLException {
        ps.setString(1, c.getTitlu());
        ps.setInt(2, c.getAnPublicare());
        ps.setInt(3, c.getNrPagini());
        ps.setBoolean(4, c.isDisponibil());
        ps.setInt(5, c.getNrImprumuturi());
        ps.setString(6, c.getCategorie());
        ps.setDouble(7, c.getRating());
        ps.setString(8, String.join(",", c.getAutori()));
    }

    @Override
    public void insertSecondary(Connection c, Carte carte, int id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO carte(id,isbn,editura_id) VALUES(?,?,?)")) {
            ps.setInt(1, id);
            ps.setString(2, carte.getISBN());
            ps.setInt(3, carte.getEditura().id());
            ps.executeUpdate();
        }
    }
}
