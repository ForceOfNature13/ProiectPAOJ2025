package util;

import model.Publicatie;

public class ComparatorPublicatieDupaTitlu implements PublicatieSortStrategy {

    @Override
    public int compare(Publicatie p1, Publicatie p2) {
        if (p1.getTitlu() == null && p2.getTitlu() == null) return 0;
        if (p1.getTitlu() == null) return -1;
        if (p2.getTitlu() == null) return  1;

        return p1.getTitlu().compareToIgnoreCase(p2.getTitlu());
    }
}
