package exceptie;

public enum CodAutentificare {
    USER_INEXISTENT("Username inexistent!"),
    PAROLA_GRESITA("Parola incorecta!"),
    CONT_BLOCAT("Contul blocat. Contacteaza personalul bibliotecii!");

    private final String mesaj;
    CodAutentificare(String m) { this.mesaj = m; }
    public String getMesaj() { return this.mesaj; }

}
