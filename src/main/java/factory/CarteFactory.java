package factory;

import model.*;
import java.util.ArrayList;

public class CarteFactory implements PublicatieFactory {
    @Override
    public Publicatie create(PublicatieDTO d) {
        return new Carte(
                d.titlu(),
                d.an(),
                d.pagini(),
                true,
                new ArrayList<>(),
                d.autori(),
                (String)  d.extra().get("isbn"),
                (Editura) d.extra().get("editura"),
                (String)  d.extra().get("categorie")
        );
    }
}
