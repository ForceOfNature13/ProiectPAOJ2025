package exceptie;
public class AccesInterzisExceptie extends ExceptieAplicatie {
    public AccesInterzisExceptie() { super("Acces interzis!"); }

    public AccesInterzisExceptie(String s) {
        super(s);
    }
}
