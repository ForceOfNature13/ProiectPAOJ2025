package util;

import model.Publicatie;
import java.util.Comparator;

public class ComparatorPublicatieDupaAn implements Comparator<Publicatie> {
    @Override
    public int compare(Publicatie p1, Publicatie p2) {
        return Integer.compare(p1.getAnPublicare(), p2.getAnPublicare());
    }
}
