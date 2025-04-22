package util;

import model.Publicatie;
import model.Cititor;

public interface ImprumutValidationHandler {
    void setNext(ImprumutValidationHandler next);
    void validate(Publicatie p, Cititor c) throws Exception;
}
