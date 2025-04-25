package model;

import exceptie.InputInvalidExceptie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Recenzie  implements Identifiable {
    private int id;
    private final int idPublicatie;
    private final int idCititor;
    private int rating;
    private final String comentariu;
    private final LocalDateTime data;

    public Recenzie(int idPublicatie, int idCititor, int rating, String comentariu, LocalDateTime data)
            throws InputInvalidExceptie {
        this.id =0;
        this.idPublicatie = idPublicatie;
        this.idCititor = idCititor;
        setRating(rating);
        this.comentariu = comentariu;
        this.data = data;
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

    public int getIdPublicatie() {
        return this.idPublicatie;
    }

    public int getIdCititor() {
        return this.idCititor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComentariu() {
        return this.comentariu;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
