package exceptie;

public class LimitaDepasitaExceptie extends ExceptieAplicatie {
    private final TipLimita tip;
    private final int curent;
    private final int maxim;

    public LimitaDepasitaExceptie(TipLimita tip, int curent, int maxim) {
        super("Limita pentru " + tip.getText()
                + " a fost depasita (" + curent + "/" + maxim + ").");
        this.tip = tip;
        this.curent = curent;
        this.maxim = maxim;
    }
    public TipLimita getTip()  { return tip;    }
    public int getCurent()     { return curent; }
    public int getMaxim()      { return maxim;  }
}
