package factory;

import model.Publicatie;

public interface PublicatieFactory {
    Publicatie create(PublicatieDTO dto);
}
