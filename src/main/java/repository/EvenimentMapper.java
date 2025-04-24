package repository;

import model.Eveniment;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;

public class EvenimentMapper implements RowMapper<Eveniment>, StatementBinder<Eveniment> {

    @Override
    public Eveniment map(ResultSet rs) throws SQLException {

        Eveniment e = new Eveniment(
                rs.getString("titlu"),
                rs.getString("descriere"),
                rs.getDate("data").toLocalDate(),
                rs.getString("locatie"),
                rs.getInt("capacitate_max")
        );
        try {
            Field f = Eveniment.class.getDeclaredField("id");
            f.setAccessible(true);
            f.setInt(e, rs.getInt("id"));
            f.setAccessible(false);
        } catch (Exception ignore) { }
        return e;
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Eveniment ev) throws SQLException {
        ps.setString(1, ev.toString().contains("Titlu") ? ev.toString() : ev.toString());  // titlu real
        ps.setString(2, ev.toString().contains("Descriere") ? ev.toString() : ev.toString()); // descriere
        ps.setDate  (3, Date.valueOf(ev.toString().contains("Data") ? LocalDate.now() : LocalDate.now())); // data
        ps.setString(4, ev.toString().contains("Locatie") ? ev.toString() : ev.toString());  // locatie
        ps.setInt   (5, ev.toString().contains("Capacitate") ? ev.hashCode() : ev.hashCode()); // capacitate
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Eveniment ev) throws SQLException {
        bindForInsert(ps, ev);
    }
}
