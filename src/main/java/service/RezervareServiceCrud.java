package service;
import model.Rezervare;
import repository.RezervareMapper;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RezervareServiceCrud {

    private final RezervareMapper mapper = new RezervareMapper();
    private RezervareServiceCrud() { }
    private static final RezervareServiceCrud I = new RezervareServiceCrud();
    public static RezervareServiceCrud getInstance() { return I; }

    public void create(Rezervare r) {
        try (Connection c = ConnectionManager.get().open();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO rezervare(publicatie_id,cititor_id,data_rezervare,pozitie) " +
                             "VALUES (?,?,?,?)")) {
            mapper.bindForInsert(ps, r);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la insert rezervare", e);
        }
    }

    public void delete(int publicatieId, int cititorId) {
        try (Connection c = ConnectionManager.get().open();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM rezervare WHERE publicatie_id=? AND cititor_id=?")) {
            ps.setInt(1, publicatieId);
            ps.setInt(2, cititorId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la delete rezervare", e);
        }
    }

    public List<Rezervare> readAll() {
        try (Connection c = ConnectionManager.get().open();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM rezervare")) {

            List<Rezervare> list = new ArrayList<>();
            while (rs.next()) list.add(mapper.map(rs));
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Eroare la read rezervare", e);
        }
    }
    public void updatePosition(int publicatieId, int cititorId, int nouaPozitie) {
        try (Connection c = ConnectionManager.get().open();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE rezervare SET pozitie=? " +
                             "WHERE publicatie_id=? AND cititor_id=?")) {
            ps.setInt(1, nouaPozitie);
            ps.setInt(2, publicatieId);
            ps.setInt(3, cititorId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la update pozitie rezervare", e);
        }
    }

}
