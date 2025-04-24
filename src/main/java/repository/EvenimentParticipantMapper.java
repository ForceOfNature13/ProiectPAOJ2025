package repository;

import model.EvenimentParticipant;
import java.sql.*;

public class EvenimentParticipantMapper implements RowMapper<EvenimentParticipant>,
        StatementBinder<EvenimentParticipant> {

    @Override
    public EvenimentParticipant map(ResultSet rs) throws SQLException {
        return new EvenimentParticipant(
                rs.getInt("eveniment_id"),
                rs.getInt("cititor_id")
        );
    }


    @Override
    public void bindForInsert(PreparedStatement ps,
                              EvenimentParticipant ep) throws SQLException {
        ps.setInt(1, ep.evenimentId());
        ps.setInt(2, ep.cititorId());
    }

    @Override public void bindForUpdate(PreparedStatement ps,
                                        EvenimentParticipant ep) { }
}
