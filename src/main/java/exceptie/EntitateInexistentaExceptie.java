package exceptie;

public class EntitateInexistentaExceptie extends ExceptieAplicatie {
    private final int id;

    public EntitateInexistentaExceptie(String tip, int id) {
        super(tip + " cu ID " + id + " nu exista.");
        this.id = id;
    }

    public int getId()     { return id;  }
}
