package factory;

import java.util.List;
import java.util.Map;

public record PublicatieDTO(
        String titlu,
        int    an,
        int    pagini,
        List<String> autori,
        Map<String,Object> extra
) {}
