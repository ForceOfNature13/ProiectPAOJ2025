package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementBinder<T> {
    void bindForInsert(PreparedStatement ps, T obj) throws SQLException;
    void bindForUpdate(PreparedStatement ps, T obj) throws SQLException;
    default void insertSecondary(Connection c, T obj, int id) throws SQLException { }
}
