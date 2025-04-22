package model;

import exceptie.ResursaIndisponibilaExceptie;

public interface Imprumutabil {
    void imprumuta() throws ResursaIndisponibilaExceptie;
    void returneaza();
    int durataMaximaZile();
    default double penalizarePeZi() {
        return 1.5;
    }
    default double calcPenalizare(long zileIntarziere) {
        return zileIntarziere * penalizarePeZi();
    }
}
