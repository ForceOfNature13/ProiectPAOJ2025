package model;

import exceptie.ResursaIndisponibilaExceptie;

public interface Imprumutabil {
    void imprumuta() throws ResursaIndisponibilaExceptie;
    void returneaza();
    int durataMaximaZile();
}
