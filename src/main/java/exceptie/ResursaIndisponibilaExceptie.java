package exceptie;

public class ResursaIndisponibilaExceptie extends ExceptieAplicatie {

    public ResursaIndisponibilaExceptie(MotivIndisponibilitate motiv, int id) {
        super("Publicatia cu ID " + id + " nu poate fi momentan " + motiv.getText() + ".");
    }

}
