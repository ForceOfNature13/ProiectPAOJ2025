package util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public final class ConnectionManager {
    private static volatile ConnectionManager instance;
    private final Properties prop = new Properties();

    private ConnectionManager() {
        try (InputStream in = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) throw new IllegalStateException("db.properties missing");
            prop.load(in);

            Class.forName(prop.getProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver"));
        } catch (Exception e) {
            throw new RuntimeException("Eroare inițializare ConnectionManager", e);
        }
    }

    public static ConnectionManager get() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) instance = new ConnectionManager();
            }
        }
        return instance;
    }

    public Connection open() throws SQLException {
        return DriverManager.getConnection(
                prop.getProperty("jdbc.url"),
                prop.getProperty("jdbc.user"),
                prop.getProperty("jdbc.pass"));
    }
}
