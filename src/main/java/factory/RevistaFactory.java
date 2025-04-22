package factory;

import model.*;
import java.util.ArrayList;

public class RevistaFactory implements PublicatieFactory {
    @Override
    public Publicatie create(PublicatieDTO d) {
        return new Revista(
                d.titlu(),
                d.an(),
                d.pagini(),
                true,
                new ArrayList<>(),
                (String)  d.extra().get("frecventa"),
                (Integer) d.extra().get("numar"),
                (String)  d.extra().get("categorie"),
                d.autori()
        );
    }
}
