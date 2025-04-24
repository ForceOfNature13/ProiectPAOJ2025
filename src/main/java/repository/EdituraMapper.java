package repository;

import model.Editura;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EdituraMapper implements RowMapper<Editura>, StatementBinder<Editura> {

    @Override
    public Editura map(ResultSet rs) throws SQLException {
        return new Editura(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("tara")
        );
    }

    @Override
    public void bindForInsert(PreparedStatement ps, Editura e) throws SQLException {
        ps.setString(1, e.nume());
        ps.setString(2, e.tara());
    }

    @Override
    public void bindForUpdate(PreparedStatement ps, Editura e) throws SQLException {
        ps.setString(1, e.nume());
        ps.setString(2, e.tara());
    }
}
