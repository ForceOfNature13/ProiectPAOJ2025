package util;

import model.Publicatie;
import model.Recenzie;
import java.util.Comparator;
import java.util.List;

public class ComparatorPublicatieDupaRating implements Comparator<Publicatie> {
    @Override
    public int compare(Publicatie p1, Publicatie p2) {
        double rating1 = calculeazaRatingMediu(p1);
        double rating2 = calculeazaRatingMediu(p2);
        return Double.compare(rating1, rating2);
    }

    private double calculeazaRatingMediu(Publicatie p) {
        List<Recenzie> recenzii = p.getListaRecenzii();
        if (recenzii == null || recenzii.isEmpty()) {
            return 0;
        }
        return recenzii.stream()
                .mapToInt(Recenzie::getRating)
                .average()
                .orElse(0);
    }
}
