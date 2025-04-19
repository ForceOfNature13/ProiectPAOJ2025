package exceptie;

public enum TipLimita {
    IMPRUMUTURI_ACTIVE("numar maxim de imprumuturi active"),
    REINNOIRI("numar maxim de reinnoiri"),
    CAPACITATE_EVENIMENT("capacitatea maxima a evenimentului"),
    COADA_REZERVARE("dimensiunea maxima a cozii de rezervare");

    private final String text;
    TipLimita(String t) { text = t; }
    public String getText() { return text; }
}
