package util;

import model.Publicatie;
import java.util.List;

public class SortContext {
    private PublicatieSortStrategy strategy;

    public SortContext(PublicatieSortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PublicatieSortStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Publicatie> sort(List<Publicatie> lista) {
        return lista.stream().sorted(strategy).toList();
    }
}
