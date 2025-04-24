package service;

import model.EvenimentParticipant;
import repository.EvenimentParticipantMapper;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenimentParticipantServiceCrud {

    private final EvenimentParticipantMapper mapper = new EvenimentParticipantMapper();

    private EvenimentParticipantServiceCrud() { }
    private static final EvenimentParticipantServiceCrud
            INSTANCE = new EvenimentParticipantServiceCrud();
    public static EvenimentParticipantServiceCrud getInstance() { return INSTANCE; }

    /* CREATE */
    public void create(EvenimentParticipant ep) {
        try (Connection c = ConnectionManager.get().open();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO eveniment_participant(eveniment_id,cititor_id) VALUES(?,?)")) {
            mapper.bindForInsert(ps, ep);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error inserting participant", e);
        }
    }

    /* READ ALL */
    public List<EvenimentParticipant> readAll() {
        try (Connection c = ConnectionManager.get().open();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM eveniment_participant")) {

            List<EvenimentParticipant> list = new ArrayList<>();
            while (rs.next()) list.add(mapper.map(rs));
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error reading participants", e);
        }
    }
    
    public void delete(int evenimentId, int cititorId) {
        try (Connection c = ConnectionManager.get().open();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM eveniment_participant " +
                             "WHERE eveniment_id=? AND cititor_id=?")) {
            ps.setInt(1, evenimentId);
            ps.setInt(2, cititorId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting participant", e);
        }
    }
}
