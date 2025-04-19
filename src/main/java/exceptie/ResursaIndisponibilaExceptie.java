package exceptie;

public class ResursaIndisponibilaExceptie extends ExceptieAplicatie {
    private final MotivIndisponibilitate motiv;
    private final int idPublicatie;

    public ResursaIndisponibilaExceptie(MotivIndisponibilitate motiv, int id) {
        super("Publicatia cu ID " + id + " este " + motiv.getText() + ".");
        this.motiv = motiv;
        this.idPublicatie = id;
    }
    public MotivIndisponibilitate getMotiv() { return motiv; }
    public int getIdPublicatie()             { return idPublicatie; }
}
