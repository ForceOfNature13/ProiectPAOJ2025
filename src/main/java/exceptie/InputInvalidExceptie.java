package exceptie;

public class InputInvalidExceptie extends ExceptieAplicatie {
    public InputInvalidExceptie(String camp, Object valoare) {
        super("Valoare invalida pentru campul '" + camp + "': " + valoare);
    }
}
