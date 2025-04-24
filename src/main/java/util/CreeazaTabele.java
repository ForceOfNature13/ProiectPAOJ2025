package util;

import java.sql.Connection;
import java.sql.Statement;

public final class CreeazaTabele {
    private CreeazaTabele() {}

    private static final String[] DDL = {
            """
        CREATE TABLE IF NOT EXISTS editura (
            id   INT AUTO_INCREMENT PRIMARY KEY,
            nume VARCHAR(150) NOT NULL,
            tara VARCHAR(80)  NOT NULL
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS persoana (
            id        INT AUTO_INCREMENT PRIMARY KEY,
            nume      VARCHAR(100) NOT NULL,
            prenume   VARCHAR(100) NOT NULL,
            email     VARCHAR(120) NOT NULL UNIQUE,
            telefon   VARCHAR(30),
            username  VARCHAR(60)  NOT NULL UNIQUE,
            parola    VARCHAR(255) NOT NULL,
            blocat    BOOLEAN      DEFAULT FALSE
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS cititor (
            id                 INT PRIMARY KEY,
            adresa             VARCHAR(255) NOT NULL,
            nr_max_imprumuturi INT          NOT NULL,
            suma_penalizari    DECIMAL(8,2) DEFAULT 0,
            CONSTRAINT fk_cit_pers FOREIGN KEY(id)
                REFERENCES persoana(id) ON DELETE CASCADE
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS bibliotecar (
            id            INT PRIMARY KEY,
            sectie        VARCHAR(120) NOT NULL,
            data_angajare DATE         NOT NULL,
            rol           ENUM('STAFF','ADMIN') NOT NULL,
            CONSTRAINT fk_bib_pers FOREIGN KEY(id)
                REFERENCES persoana(id) ON DELETE CASCADE
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS publicatie (
            id              INT AUTO_INCREMENT PRIMARY KEY,
            titlu           VARCHAR(200) NOT NULL,
            an_publicare    YEAR         NOT NULL,
            nr_pagini       INT          NOT NULL,
            disponibil      BOOLEAN      DEFAULT TRUE,
            nr_imprumuturi  INT          DEFAULT 0,
            categorie       VARCHAR(100),
            rating          DECIMAL(3,2) DEFAULT 0,
            autori          TEXT         NOT NULL
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS carte (
            id         INT PRIMARY KEY,
            isbn       VARCHAR(20) NOT NULL UNIQUE,
            editura_id INT NOT NULL,
            CONSTRAINT fk_carte_pub FOREIGN KEY(id)
                REFERENCES publicatie(id) ON DELETE CASCADE,
            CONSTRAINT fk_carte_edit FOREIGN KEY(editura_id)
                REFERENCES editura(id)
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS revista (
            id        INT PRIMARY KEY,
            frecventa VARCHAR(30) NOT NULL,
            numar     INT         NOT NULL,
            CONSTRAINT fk_rev_pub FOREIGN KEY(id)
                REFERENCES publicatie(id) ON DELETE CASCADE
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS audiobook (
            id     INT PRIMARY KEY,
            durata INT         NOT NULL,
            format VARCHAR(20) NOT NULL,
            CONSTRAINT fk_audio_pub FOREIGN KEY(id)
                REFERENCES publicatie(id) ON DELETE CASCADE
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS imprumut (
            id               INT AUTO_INCREMENT PRIMARY KEY,
            publicatie_id    INT NOT NULL,
            cititor_id       INT NOT NULL,
            data_imprumut    DATE NOT NULL,
            data_scadenta    DATE NOT NULL,
            data_returnare   DATE,
            numar_reinnoiri  INT          DEFAULT 0,
            penalitate       DECIMAL(8,2) DEFAULT 0,
            CONSTRAINT fk_imp_pub FOREIGN KEY(publicatie_id)
                REFERENCES publicatie(id) ON DELETE CASCADE,
            CONSTRAINT fk_imp_cit FOREIGN KEY(cititor_id)
                REFERENCES cititor(id)    ON DELETE CASCADE
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS recenzie (
            id            INT AUTO_INCREMENT PRIMARY KEY,
            publicatie_id INT NOT NULL,
            cititor_id    INT NOT NULL,
            rating        TINYINT CHECK (rating BETWEEN 1 AND 5),
            comentariu    TEXT,
            data          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_rec_pub FOREIGN KEY(publicatie_id)
                REFERENCES publicatie(id) ON DELETE CASCADE,
            CONSTRAINT fk_rec_cit FOREIGN KEY(cititor_id)
                REFERENCES cititor(id)    ON DELETE CASCADE
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS rezervare (
            publicatie_id  INT,
            cititor_id     INT,
            data_rezervare TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            pozitie        INT,
            PRIMARY KEY(publicatie_id, cititor_id),
            CONSTRAINT fk_rz_pub FOREIGN KEY(publicatie_id)
                REFERENCES publicatie(id) ON DELETE CASCADE,
            CONSTRAINT fk_rz_cit FOREIGN KEY(cititor_id)
                REFERENCES cititor(id)    ON DELETE CASCADE
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS eveniment (
            id             INT AUTO_INCREMENT PRIMARY KEY,
            titlu          VARCHAR(200) NOT NULL,
            descriere      TEXT         NOT NULL,
            data           DATE         NOT NULL,
            locatie        VARCHAR(150) NOT NULL,
            capacitate_max INT          NOT NULL
        );
        """,
            """
        CREATE TABLE IF NOT EXISTS eveniment_participant (
            eveniment_id INT,
            cititor_id   INT,
            PRIMARY KEY(eveniment_id, cititor_id),
            CONSTRAINT fk_ep_ev  FOREIGN KEY(eveniment_id)
                REFERENCES eveniment(id) ON DELETE CASCADE,
            CONSTRAINT fk_ep_cit FOREIGN KEY(cititor_id)
                REFERENCES cititor(id)   ON DELETE CASCADE
        );
        """
    };

    public static void creazaSchema() {
        try (Connection c = ConnectionManager.get().open();
             Statement st = c.createStatement()) {

            for (String sql : DDL) st.execute(sql);

            st.executeUpdate("""
                INSERT IGNORE INTO editura(id, nume, tara)
                VALUES (1, 'EdituraX', 'Romania');
            """);

        } catch (Exception e) {
            throw new RuntimeException("Eroare la crearea schemei", e);
        }
    }
}
