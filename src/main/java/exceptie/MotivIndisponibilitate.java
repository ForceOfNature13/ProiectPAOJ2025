package exceptie;

public enum MotivIndisponibilitate {
    IMPRUMUTATA("imprumutata"),
    REZERVATA("rezervata");

    private final String text;
    MotivIndisponibilitate(String t) { text = t; }
    public String getText() { return text; }
}
