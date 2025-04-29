package model;

import java.sql.Timestamp;

public record Rezervare(int publicatieId,
                        int cititorId,
                        Timestamp dataRezervare,
                        int pozitie) {}