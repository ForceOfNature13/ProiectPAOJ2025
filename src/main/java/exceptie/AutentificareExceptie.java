package exceptie;

public class AutentificareExceptie extends ExceptieAplicatie {
    private final CodAutentificare cod;
    public AutentificareExceptie(CodAutentificare cod) {
        super(cod.getMesaj());
        this.cod = cod;
    }
    public CodAutentificare getCod() { return cod; }
}
