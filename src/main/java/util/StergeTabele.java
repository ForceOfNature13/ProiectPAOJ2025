package util;

import java.sql.Connection;
import java.sql.Statement;

public final class StergeTabele {
    private StergeTabele() {}

    private static final String[] DROP = {
            "DROP TABLE IF EXISTS eveniment_participant;",
            "DROP TABLE IF EXISTS eveniment;",
            "DROP TABLE IF EXISTS rezervare;",
            "DROP TABLE IF EXISTS recenzie;",
            "DROP TABLE IF EXISTS imprumut;",
            "DROP TABLE IF EXISTS audiobook;",
            "DROP TABLE IF EXISTS revista;",
            "DROP TABLE IF EXISTS carte;",
            "DROP TABLE IF EXISTS publicatie;",
            "DROP TABLE IF EXISTS bibliotecar;",
            "DROP TABLE IF EXISTS cititor;",
            "DROP TABLE IF EXISTS persoana;",
            "DROP TABLE IF EXISTS editura;"
    };

    public static void stergeSchema() {
        try (Connection c = ConnectionManager.get().open();
             Statement st = c.createStatement()) {

            st.execute("SET FOREIGN_KEY_CHECKS = 0");
            for (String sql : DROP) st.execute(sql);
            st.execute("SET FOREIGN_KEY_CHECKS = 1");

        } catch (Exception e) {
            throw new RuntimeException("Eroare la stergerea schemei", e);
        }
    }
}
