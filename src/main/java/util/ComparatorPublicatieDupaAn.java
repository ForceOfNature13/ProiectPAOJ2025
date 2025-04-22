package util;

import model.Publicatie;

public class ComparatorPublicatieDupaAn implements PublicatieSortStrategy {
    @Override
    public int compare(Publicatie p1, Publicatie p2) {
        return Integer.compare(p2.getAnPublicare(), p1.getAnPublicare());
    }
}