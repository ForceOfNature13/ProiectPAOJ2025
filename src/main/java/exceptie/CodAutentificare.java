package exceptie;

public enum CodAutentificare {
    USER_INEXISTENT("Username inexistent!"),
    USER_EXISTENT("Username deja folosit!"),
    PAROLA_GRESITA("Parola incorecta!");

    private final String mesaj;
    CodAutentificare(String m) { mesaj = m; }
    public String getMesaj() { return mesaj; }
}
