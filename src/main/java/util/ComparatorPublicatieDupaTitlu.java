package util;

import model.Publicatie;
import java.util.Comparator;

public class ComparatorPublicatieDupaTitlu implements Comparator<Publicatie> {

    @Override
    public int compare(Publicatie p1, Publicatie p2) {
        if (p1.getTitlu() == null && p2.getTitlu() == null) return 0;
        if (p1.getTitlu() == null) return -1;
        if (p2.getTitlu() == null) return  1;

        return p1.getTitlu().compareToIgnoreCase(p2.getTitlu());
    }
}
