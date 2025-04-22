package util;

import model.Publicatie;

public class ComparatorPublicatieDupaNrImprumuturi implements PublicatieSortStrategy {
    @Override
    public int compare(Publicatie p1, Publicatie p2) {
        return Integer.compare(p2.getNrImprumuturi(), p1.getNrImprumuturi());
    }
}
