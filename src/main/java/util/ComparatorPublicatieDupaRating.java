package util;

import model.Publicatie;

public class ComparatorPublicatieDupaRating implements PublicatieSortStrategy {
    @Override
    public int compare(Publicatie p1, Publicatie p2) {
        return Double.compare(p2.getRating(), p1.getRating());
    }
}