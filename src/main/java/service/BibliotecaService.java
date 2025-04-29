package service;

import audit.AuditAction;
import exceptie.*;
import model.*;
import observer.EventBus;
import util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

public class BibliotecaService {

    private static BibliotecaService instance;
    public  static BibliotecaService getInstance() {
        if (instance == null) instance = new BibliotecaService();
        return instance;
    }
    private BibliotecaService() { preloadCache(); }

    private final Map<Integer, Publicatie>  publicatii    = new HashMap<>();
    private final Map<Integer, Cititor>     cititori      = new HashMap<>();
    private final Map<Integer, Bibliotecar> bibliotecari  = new HashMap<>();
    private final List<Imprumut>            imprumuturi   = new ArrayList<>();
    private final Map<Integer, RezervarePublicatie> rezervari = new HashMap<>();


    private final SortContext sortContext = new SortContext(new ComparatorPublicatieDupaAn());


    private final ImprumutValidationHandler imprumutChain;
    private final ImprumutValidationHandler reinnoireChain;
    private final ImprumutValidationHandler rezervareChain;
    {
        LimitaImprumuturiHandler h1 = new LimitaImprumuturiHandler();
        RezervareConflictHandler h2 = new RezervareConflictHandler(rezervari);
        h1.setNext(h2);
        imprumutChain = h1;

        MaxReinnoiriHandler r1 = new MaxReinnoiriHandler();
        RezervareConflictHandler r2 = new RezervareConflictHandler(rezervari);
        r1.setNext(r2);
        reinnoireChain = r1;

        rezervareChain = new CoadaRezervariPlinaHandler(rezervari);
    }


    private void preloadCache() {
        publicatii.clear();
        CarteServiceCrud    .getInstance().readAll().forEach(p -> publicatii.put(p.getId(), p));
        RevistaServiceCrud  .getInstance().readAll().forEach(p -> publicatii.put(p.getId(), p));
        AudiobookServiceCrud.getInstance().readAll().forEach(p -> publicatii.put(p.getId(), p));

        cititori.clear();
        CititorServiceCrud.getInstance().readAll()
                .forEach(c -> cititori.put(c.getId(), c));

        bibliotecari.clear();
        BibliotecarServiceCrud.getInstance().readAll()
                .forEach(b -> bibliotecari.put(b.getId(), b));

        imprumuturi.clear();
        imprumuturi.addAll(ImprumutServiceCrud.getInstance().readAll());
    }



    public void adaugaPublicatie(Publicatie p) {
        switch (p) {
            case Carte c -> CarteServiceCrud.getInstance().create(c);
            case Revista r -> RevistaServiceCrud.getInstance().create(r);
            case Audiobook a -> AudiobookServiceCrud.getInstance().create(a);
            case null, default -> throw new IllegalArgumentException("Tip necunoscut");
        }
        publicatii.put(p.getId(), p);
        EventBus.publish(AuditAction.PUBLICATIE_CREATA);
    }


    public void stergePublicatie(int id) {
        CarteServiceCrud    .getInstance().delete(id);
        RevistaServiceCrud  .getInstance().delete(id);
        AudiobookServiceCrud.getInstance().delete(id);
        publicatii.remove(id);
        EventBus.publish(AuditAction.PUBLICATIE_STEARSA);
    }


    public Publicatie getPublicatieById(int id) throws EntitateInexistentaExceptie {
        Publicatie p = publicatii.get(id);
        if (p != null) return p;

        Optional<Publicatie> opt =
                CarteServiceCrud   .getInstance().read(id).map(x -> (Publicatie) x)
                        .or(() -> RevistaServiceCrud .getInstance().read(id).map(x -> (Publicatie) x))
                        .or(() -> AudiobookServiceCrud.getInstance().read(id).map(x -> (Publicatie) x));

        p = opt.orElseThrow(() -> new EntitateInexistentaExceptie("Publicatie", id));
        publicatii.put(id, p);
        return p;
    }

    public List<Publicatie> getToatePublicatiile() {
        List<Publicatie> list = new ArrayList<>();
        list.addAll(CarteServiceCrud   .getInstance().readAll());
        list.addAll(RevistaServiceCrud .getInstance().readAll());
        list.addAll(AudiobookServiceCrud.getInstance().readAll());
        publicatii.clear();
        list.forEach(p -> publicatii.put(p.getId(), p));
        EventBus.publish(AuditAction.LISTARE_PUBLICATII);
        return list;
    }

    public List<Publicatie> cautaDupaTitlu(String t) {
        EventBus.publish(AuditAction.CAUTARE_TITLU);
        return publicatii.values().stream()
                .filter(p -> p.getTitlu() != null &&
                        p.getTitlu().toLowerCase().contains(t.toLowerCase()))
                .toList();
    }

    public List<Publicatie> cautaDupaCategorie(String c) {
        EventBus.publish(AuditAction.CAUTARE_CATEGORIE);
        return publicatii.values().stream()
                .filter(p -> p.getCategorie() != null &&
                        p.getCategorie().equalsIgnoreCase(c))
                .toList();
    }

    public List<Publicatie> cautaDupaIntervalAni(int s, int e) {
        EventBus.publish(AuditAction.CAUTARE_INTERVAL_ANI);
        return publicatii.values().stream()
                .filter(p -> p.getAnPublicare() >= s && p.getAnPublicare() <= e)
                .toList();
    }

    public List<Publicatie> cautaDupaDisponibilitate(boolean disponibil) {
        EventBus.publish(AuditAction.CAUTARE_DISPONIBILITATE);
        return publicatii.values().stream()
                .filter(p -> p.isDisponibil() == disponibil)
                .toList();
    }

    private Comparator<Publicatie> construiesteComparator(List<String> criterii) {
        Map<String, Comparator<Publicatie>> map = Map.of(
                "an",            new ComparatorPublicatieDupaAn(),
                "rating",        new ComparatorPublicatieDupaRating(),
                "nrimprumuturi", new ComparatorPublicatieDupaNrImprumuturi(),
                "titlu",         new ComparatorPublicatieDupaTitlu()
        );
        Comparator<Publicatie> comp = null;
        for (String crt : criterii) {
            Comparator<Publicatie> add = map.get(crt.toLowerCase());
            if (add != null) comp = (comp == null) ? add : comp.thenComparing(add);
        }
        return comp;
    }

    public List<Publicatie> cautareComplexa(String titlu, String autor, String categorie,
                                            Integer anStart, Integer anEnd,
                                            List<String> criteriiSortare) {
        EventBus.publish(AuditAction.CAUTARE_COMPLEXA);
        Stream<Publicatie> s = publicatii.values().stream();
        if (titlu != null && !titlu.isBlank())
            s = s.filter(p -> p.getTitlu() != null &&
                    p.getTitlu().toLowerCase().contains(titlu.toLowerCase()));
        if (autor != null && !autor.isBlank())
            s = s.filter(p -> p.getAutori() != null &&
                    p.getAutori().stream().anyMatch(a -> a.toLowerCase().contains(autor.toLowerCase())));
        if (categorie != null && !categorie.isBlank())
            s = s.filter(p -> p.getCategorie() != null &&
                    p.getCategorie().toLowerCase().contains(categorie.toLowerCase()));
        if (anStart != null) s = s.filter(p -> p.getAnPublicare() >= anStart);
        if (anEnd   != null) s = s.filter(p -> p.getAnPublicare() <= anEnd);

        if (criteriiSortare != null && !criteriiSortare.isEmpty()) {
            Comparator<Publicatie> comp = construiesteComparator(criteriiSortare);
            if (comp != null) s = s.sorted(comp);
        }
        return s.toList();
    }

    public List<Publicatie> sorteazaDupaAn() {
        EventBus.publish(AuditAction.SORTARE_AN);
        sortContext.setStrategy(new ComparatorPublicatieDupaAn());
        return sortContext.sort(new ArrayList<>(publicatii.values()));
    }

    public List<Publicatie> sorteazaDupaRating() {
        EventBus.publish(AuditAction.SORTARE_RATING);
        sortContext.setStrategy(new ComparatorPublicatieDupaRating());
        return sortContext.sort(new ArrayList<>(publicatii.values()));
    }

    public List<Publicatie> sorteazaDupaNrImprumuturi() {
        EventBus.publish(AuditAction.SORTARE_NR_IMPRUMUTURI);
        sortContext.setStrategy(new ComparatorPublicatieDupaNrImprumuturi());
        return sortContext.sort(new ArrayList<>(publicatii.values()));
    }

    public List<Publicatie> sorteazaDupaTitlu() {
        EventBus.publish(AuditAction.SORTARE_TITLU);
        sortContext.setStrategy(new ComparatorPublicatieDupaTitlu());
        return sortContext.sort(new ArrayList<>(publicatii.values()));
    }

    public void imprumutaPublicatie(int idPub, int idCit) throws Exception {

        Publicatie p = getPublicatieById(idPub);
        Cititor    c = CititorServiceCrud.getInstance()
                .read(idCit)
                .orElseThrow(() ->
                        new EntitateInexistentaExceptie("Cititor", idCit));

        imprumutChain.validate(p, c);

        p.imprumuta();

        Imprumut impr = new Imprumut(idPub, idCit);
        ImprumutServiceCrud.getInstance().create(impr);
        //test penalizare
       //  impr.setDataScadenta(java.time.LocalDate.now().minusDays(5));
      // ImprumutServiceCrud.getInstance().update(impr);

        try (Connection con = ConnectionManager.get().open();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE publicatie SET disponibil = 0, " +
                             "nr_imprumuturi = nr_imprumuturi + 1 WHERE id = ?")) {
            ps.setInt(1, idPub);
            ps.executeUpdate();
        }

        c.adaugaImprumutActiv(impr);

        AuthService auth = AuthService.getInstance();
        if (auth.getUtilizatorCurent() instanceof Cititor cur &&
                cur.getId() == idCit) {
            cur.adaugaImprumutActiv(impr);
        }
        EventBus.publish(AuditAction.PUBLICATIE_IMPRUMUTATA);
    }

    public void returneazaPublicatie(int idPub, int idCit) throws Exception {

        Publicatie p = getPublicatieById(idPub);

        Imprumut impr = ImprumutServiceCrud.getInstance()
                .readAll()
                .stream()
                .filter(i -> i.getIdPublicatie() == idPub
                        && i.getIdCititor()   == idCit
                        && i.getDataReturnare() == null)
                .findFirst()
                .orElseThrow(() ->
                        new EntitateInexistentaExceptie("Imprumut", idPub));

        Cititor c = CititorServiceCrud.getInstance()
                .read(idCit)
                .orElseThrow(() ->
                        new EntitateInexistentaExceptie("Cititor", idCit));

        p.returneaza();
        impr.setDataReturnare(LocalDate.now());

        long zile = ChronoUnit.DAYS.between(
                impr.getDataScadenta(), impr.getDataReturnare());

        if (zile > 0) {
            double pen = p.calcPenalizare(zile);
            impr.setPenalitate(pen);
            c.adaugaPenalizare(pen);
        }

        c.stergeImprumutActiv(impr);
        c.adaugaInIstoric(impr);

        ImprumutServiceCrud.getInstance().update(impr);
        CititorServiceCrud .getInstance().update(c);

        try (Connection con = ConnectionManager.get().open();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE publicatie SET disponibil = 1 WHERE id = ?")) {
            ps.setInt(1, idPub);
            ps.executeUpdate();
        }

        proceseazaRezervareLaReturnare(idPub);

        AuthService auth = AuthService.getInstance();
        if (auth.getUtilizatorCurent() instanceof Cititor cur &&
                cur.getId() == idCit) {

            Imprumut imprCur = cur.gasesteImprumutActiv(idPub);
            if (imprCur != null) {
                imprCur.setDataReturnare(impr.getDataReturnare());
                imprCur.setPenalitate   (impr.getPenalitate());

                cur.stergeImprumutActiv(imprCur);
                cur.adaugaInIstoric(imprCur);
            }
            if (zile > 0) cur.adaugaPenalizare(impr.getPenalitate());
        }


        EventBus.publish(AuditAction.PUBLICATIE_RETURNATA);
    }


    public void reinnoiesteImprumut(int idPub, int idCit) throws Exception {

        Publicatie p = getPublicatieById(idPub);

        Cititor c = CititorServiceCrud.getInstance()
                .read(idCit)
                .orElseThrow(() ->
                        new EntitateInexistentaExceptie("Cititor", idCit));

        Imprumut impr = ImprumutServiceCrud.getInstance()
                .readAll()
                .stream()
                .filter(i -> i.getIdPublicatie() == idPub
                        && i.getIdCititor()   == idCit
                        && i.getDataReturnare() == null)
                .findFirst()
                .orElseThrow(() ->
                        new EntitateInexistentaExceptie("Imprumut", idPub));

        c.adaugaImprumutActiv(impr);

        reinnoireChain.validate(p, c);

        impr.setDataScadenta(
                impr.getDataScadenta().plusDays(p.durataMaximaZile()));
        impr.setNumarReinnoiri(impr.getNumarReinnoiri() + 1);

        ImprumutServiceCrud.getInstance().update(impr);

        AuthService auth = AuthService.getInstance();
        if (auth.getUtilizatorCurent() instanceof Cititor cur &&
                cur.getId() == idCit) {

            Imprumut imprCur = cur.gasesteImprumutActiv(idPub);
            if (imprCur != null) {
                imprCur.setDataScadenta(impr.getDataScadenta());
                imprCur.setNumarReinnoiri(impr.getNumarReinnoiri());
            }
        }

        EventBus.publish(AuditAction.IMPRUMUT_REINNOIT);
    }

    private void proceseazaRezervareLaReturnare(int idPub) {
        RezervarePublicatie rez = rezervari.get(idPub);
        if (rez == null || rez.esteGoala()) return;

        Cititor candidat = rez.veziPrimul();

        while (candidat != null) {

            try {
                imprumutaPublicatie(idPub, candidat.getId());
                RezervareServiceCrud.getInstance()
                        .delete(idPub, candidat.getId());

                    rez.extrageUrmatorul();
                if (rez.esteGoala()) rezervari.remove(idPub);
                return;
            }
            catch (Exception e) {
                RezervareServiceCrud.getInstance()
                        .delete(idPub, candidat.getId());
                rez.extrageUrmatorul();
                candidat = rez.veziPrimul();
            }
        }
    }



    public void rezervaPublicatie(int idPub, int idCit) throws Exception {
        Publicatie p = getPublicatieById(idPub);
        Cititor   c = CititorServiceCrud.getInstance()
                .read(idCit)
                .orElseThrow(() -> new EntitateInexistentaExceptie("Cititor", idCit));

        rezervareChain.validate(p, c);

        RezervarePublicatie rez = rezervari.computeIfAbsent(
                idPub, k -> new RezervarePublicatie(idPub));
        rez.adaugaInCoada(c);

        EventBus.publish(AuditAction.PUBLICATIE_REZERVATA);
    }

    public void adaugaRecenzie(int idPub, Recenzie r) throws EntitateInexistentaExceptie {
        RecenzieServiceCrud.getInstance().create(r);
        getPublicatieById(idPub).getListaRecenzii().add(r);
        getPublicatieById(idPub).updateRating();
        EventBus.publish(AuditAction.RECENZIE_ADAUGATA);
    }

    public double calculeazaRatingMediu(int idPub) throws EntitateInexistentaExceptie {
        return getPublicatieById(idPub).getRating();
    }

    public void adaugaCititor(Cititor c) {
        CititorServiceCrud.getInstance().create(c);
        cititori.put(c.getId(), c);
        EventBus.publish(AuditAction.CITITOR_ADAUGAT);
    }

    public void adaugaBibliotecar(Bibliotecar b) {
        BibliotecarServiceCrud.getInstance().create(b);
        bibliotecari.put(b.getId(), b);
        EventBus.publish(AuditAction.BIBLIOTECAR_ADAUGAT);
    }

    public void adaugaBibliotecarStaff(Bibliotecar b) throws AccesInterzisExceptie {
        AuthService.getInstance().verificaBibliotecar(RolBibliotecar.ADMIN);
        b.setRol(RolBibliotecar.STAFF);
        adaugaBibliotecar(b);
        EventBus.publish(AuditAction.BIBLIOTECAR_STAFF_CREAT);
    }

    public Map<Integer, Bibliotecar> getBibliotecariMap()  { return Collections.unmodifiableMap(bibliotecari); }

    public void blocheazaUtilizator(int id) throws Exception {
        AuthService.getInstance().verificaBibliotecar(RolBibliotecar.ADMIN);
        Persoana p = cautaPersoana(id);
        p.setBlocat(true);
        updatePersoana(p);
        EventBus.publish(AuditAction.UTILIZATOR_BLOCAT);
    }

    public void deblocheazaUtilizator(int id) throws Exception {
        AuthService.getInstance().verificaBibliotecar(RolBibliotecar.ADMIN);
        Persoana p = cautaPersoana(id);
        p.setBlocat(false);
        updatePersoana(p);
        EventBus.publish(AuditAction.UTILIZATOR_DEBLOCAT);
    }


    private Persoana cautaPersoana(int id) throws EntitateInexistentaExceptie {
        Optional<? extends Persoana> p =
                CititorServiceCrud   .getInstance().read(id).map(x -> (Persoana) x);
        if (p.isEmpty())
            p = BibliotecarServiceCrud.getInstance().read(id).map(x -> (Persoana) x);
        return p.orElseThrow(() -> new EntitateInexistentaExceptie("Persoana", id));
    }

    private void updatePersoana(Persoana p) {
        if (p instanceof Cititor     c) CititorServiceCrud    .getInstance().update(c);
        else if (p instanceof Bibliotecar b) BibliotecarServiceCrud.getInstance().update(b);
    }

    public List<Recenzie> getRecenziiCititor(int cititorId)
            throws EntitateInexistentaExceptie, AccesInterzisExceptie {

        Persoana p = cititori.get(cititorId);
        if (p == null)
            throw new EntitateInexistentaExceptie("Cititor inexistent!", cititorId);

        List<Recenzie> recs =
                RecenzieServiceCrud.getInstance().findByCititor(cititorId);

        EventBus.publish(AuditAction.RECENZII_CITITOR_AFISATE);
        return recs;
    }

    public List<Imprumut> getImprumuturiActiveCititor(int idCititor)
            throws EntitateInexistentaExceptie, AccesInterzisExceptie {

        Persoana p = cititori.get(idCititor);
        if (p == null)
            throw new EntitateInexistentaExceptie("Cititor", idCititor);

        List<Imprumut> active =
                ImprumutServiceCrud.getInstance().findActiveByCititor(idCititor);

        EventBus.publish(AuditAction.IMPRUMUTURI_ACTIVE_CITITOR_AFISATE);
        return active;
    }
    public List<Imprumut> getIstoricReturnatCititor(int idCititor)
            throws EntitateInexistentaExceptie, AccesInterzisExceptie {

        Persoana p = cititori.get(idCititor);
        if (p == null)
            throw new EntitateInexistentaExceptie("Cititor", idCititor);

        List<Imprumut> ist =
                ImprumutServiceCrud.getInstance().findIstoricReturnatByCititor(idCititor);

        EventBus.publish(AuditAction.IMPRUMUTURI_ISTORIC_CITITOR_AFISATE);
        return ist;
    }
    public List<Imprumut> getAmenziCititor(int idCititor)
            throws EntitateInexistentaExceptie, AccesInterzisExceptie {

        Persoana p = cititori.get(idCititor);
        if (p == null)
            throw new EntitateInexistentaExceptie("Cititor", idCititor);

        List<Imprumut> penal =
                ImprumutServiceCrud.getInstance().findPenalizateByCititor(idCititor);

        EventBus.publish(AuditAction.AMENZI_CITITOR_AFISATE);
        return penal;
    }



}
