package exceptie;

public class LimitaDepasitaExceptie extends ExceptieAplicatie {

    public LimitaDepasitaExceptie(TipLimita tip, int curent, int maxim) {
        super("Limita pentru " + tip.getText()
                + " a fost depasita (" + curent + "/" + maxim + ").");
    }

}
