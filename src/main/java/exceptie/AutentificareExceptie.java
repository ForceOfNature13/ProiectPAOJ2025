package exceptie;

public class AutentificareExceptie extends Exception {
    public AutentificareExceptie(CodAutentificare cod) {
        super(cod.getMesaj());
    }
}
