package repository;

import model.Identifiable;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenericJdbcRepository<T, ID> {

    private final String table;
    private final String idCol;
    private final String insertSql;
    private final String updateSql;
    private final RowMapper<T> mapper;
    private final StatementBinder<T> binder;

    public GenericJdbcRepository(String table,
                                 String idCol,
                                 String insertSql,
                                 String updateSql,
                                 RowMapper<T> mapper,
                                 StatementBinder<T> binder) {
        this.table     = table;
        this.idCol     = idCol;
        this.insertSql = insertSql;
        this.updateSql = updateSql;
        this.mapper    = mapper;
        this.binder    = binder;
    }

    @SuppressWarnings("unchecked")
    public ID save(T obj) {
        try (Connection c = ConnectionManager.get().open()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps =
                         c.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

                binder.bindForInsert(ps, obj);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next())
                        throw new RuntimeException("Cheia nu a fost generata");

                    Number n = (Number) keys.getObject(1);
                    ID id = (ID) Integer.valueOf(n.intValue());

                    if (obj instanceof Identifiable ident) {
                        ident.setId(n.intValue());
                    }

                    binder.insertSecondary(c, obj, n.intValue());
                    c.commit();
                    return id;
                }
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                try { c.setAutoCommit(true); } catch (SQLException ignore) { }
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la save: " + e.getMessage(), e);
        }
    }
    @SuppressWarnings("unchecked")
    public ID save(Connection c, T obj) throws SQLException {

        try (PreparedStatement ps =
                     c.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            binder.bindForInsert(ps, obj);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next())
                    throw new RuntimeException("Cheia nu a fost generata");

                Number n = (Number) keys.getObject(1);
                ID id = (ID) Integer.valueOf(n.intValue());

                if (obj instanceof Identifiable ident)
                    ident.setId(n.intValue());

                binder.insertSecondary(c, obj, n.intValue());

                return id;
            }
        }
    }

    public Optional<T> findById(ID id) {
        String sql = "SELECT * FROM " + table + " WHERE " + idCol + "=?";
        try (Connection c = ConnectionManager.get().open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapper.map(rs)) : Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la findById: " + e.getMessage(), e);
        }
    }

    public List<T> findAll() {
        String sql = "SELECT * FROM " + table;
        try (Connection c = ConnectionManager.get().open();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<T> list = new ArrayList<>();
            while (rs.next()) list.add(mapper.map(rs));
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Eroare la findAll: " + e.getMessage(), e);
        }
    }

    public void update(T obj, ID id) {
        try (Connection c = ConnectionManager.get().open();
             PreparedStatement ps = c.prepareStatement(updateSql)) {

            binder.bindForUpdate(ps, obj);
            ps.setObject(ps.getParameterMetaData().getParameterCount(), id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la update: " + e.getMessage(), e);
        }
    }

    public void deleteById(ID id) {
        String sql = "DELETE FROM " + table + " WHERE " + idCol + "=?";
        try (Connection c = ConnectionManager.get().open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la deleteById: " + e.getMessage(), e);
        }
    }
    public List<T> findMany(String sql, Object... params) {
        try (Connection c = ConnectionManager.get().open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);

            try (ResultSet rs = ps.executeQuery()) {
                List<T> list = new ArrayList<>();
                while (rs.next()) list.add(mapper.map(rs));
                return list;
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la findMany: " + e.getMessage(), e);
        }
    }
}
