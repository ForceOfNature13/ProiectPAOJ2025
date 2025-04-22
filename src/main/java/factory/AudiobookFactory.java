package factory;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class AudiobookFactory implements PublicatieFactory {
    @Override
    public Publicatie create(PublicatieDTO d) {
        @SuppressWarnings("unchecked")
        List<String> naratori = (List<String>) d.extra().get("naratori");

        return new Audiobook(
                d.titlu(),
                d.an(),
                0,
                true,
                new ArrayList<>(),
                (String)  d.extra().get("categorie"),
                d.autori(),
                (Integer) d.extra().get("durata"),
                naratori,
                (String)  d.extra().get("format")
        );
    }
}
