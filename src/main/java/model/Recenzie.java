package model;

import exceptie.InputInvalidExceptie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Recenzie {
    private static int idGenerator = 0;

    private int id;
    private int idPublicatie;
    private int idCititor;
    private int rating;
    private String comentariu;
    private LocalDateTime data;

    public Recenzie(int idPublicatie, int idCititor, int rating, String comentariu, LocalDateTime data)
            throws InputInvalidExceptie {
        this.id = ++idGenerator;
        this.idPublicatie = idPublicatie;
        this.idCititor = idCititor;
        setRating(rating);
        this.comentariu = comentariu;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getIdPublicatie() {
        return idPublicatie;
    }

    public void setIdPublicatie(int idPublicatie) {
        this.idPublicatie = idPublicatie;
    }

    public int getIdCititor() {
        return idCititor;
    }

    public void setIdCititor(int idCititor) {
        this.idCititor = idCititor;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) throws InputInvalidExceptie {
        if (rating < 1 || rating > 5) {
            throw new InputInvalidExceptie("rating", rating);
        }
        this.rating = rating;
    }

    public String getComentariu() {
        return comentariu;
    }

    public void setComentariu(String comentariu) {
        this.comentariu = comentariu;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Recenzie:\n"
                + "Id: " + id + "\n"
                + "IdPublicatie: " + idPublicatie + "\n"
                + "IdCititor: " + idCititor + "\n"
                + "Rating: " + rating + "\n"
                + "Comentariu: " + comentariu + "\n"
                + "Data: " + data.format(fmt) + "\n";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recenzie recenzie = (Recenzie) o;
        return id == recenzie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
