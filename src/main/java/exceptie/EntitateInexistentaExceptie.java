package exceptie;

public class EntitateInexistentaExceptie extends ExceptieAplicatie {
    private final String tip;
    private final int id;

    public EntitateInexistentaExceptie(String tip, int id) {
        super(tip + " cu ID " + id + " nu exista.");
        this.tip = tip;
        this.id = id;
    }
    public String getTip() { return tip; }
    public int getId()     { return id;  }
}
