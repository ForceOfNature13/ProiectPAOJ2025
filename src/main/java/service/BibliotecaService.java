package service;

import exceptie.*;
import model.*;
import util.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

import observer.EventBus;
import audit.AuditAction;

public class BibliotecaService {

    private static BibliotecaService instance;

    private final Map<Integer, Publicatie> publicatii = new HashMap<>();
    private final Map<Integer, Cititor> cititori = new HashMap<>();
    private final Map<Integer, Bibliotecar> bibliotecari = new HashMap<>();
    private final List<Imprumut> imprumuturi = new ArrayList<>();
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

    private BibliotecaService() {}

    public static BibliotecaService getInstance() {
        if (instance == null) instance = new BibliotecaService();
        return instance;
    }

    public void adaugaPublicatie(Publicatie p) {
        publicatii.put(p.getId(), p);
        EventBus.publish(AuditAction.PUBLICATIE_CREATA);
    }

    public void stergePublicatie(int id) {
        publicatii.remove(id);
        EventBus.publish(AuditAction.PUBLICATIE_STEARSA);
    }

    public Publicatie getPublicatieById(int id) throws EntitateInexistentaExceptie {
        Publicatie p = publicatii.get(id);
        if (p == null) throw new EntitateInexistentaExceptie("Publicatie", id);
        return p;
    }

    public List<Publicatie> getToatePublicatiile() {
        EventBus.publish(AuditAction.LISTARE_PUBLICATII);
        return new ArrayList<>(publicatii.values());
    }

    public List<Publicatie> cautaDupaTitlu(String t) {
        EventBus.publish(AuditAction.CAUTARE_TITLU);
        return publicatii.values().stream()
                .filter(p -> p.getTitlu() != null && p.getTitlu().toLowerCase().contains(t.toLowerCase()))
                .toList();
    }

    public List<Publicatie> cautaDupaCategorie(String c) {
        EventBus.publish(AuditAction.CAUTARE_CATEGORIE);
        return publicatii.values().stream()
                .filter(p -> p.getCategorie() != null && p.getCategorie().equalsIgnoreCase(c))
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

        Stream<Publicatie> stream = publicatii.values().stream();
        if (titlu != null && !titlu.isBlank()) {
            String tl = titlu.toLowerCase();
            stream = stream.filter(p -> p.getTitlu() != null &&
                    p.getTitlu().toLowerCase().contains(tl));
        }
        if (autor != null && !autor.isBlank()) {
            String al = autor.toLowerCase();
            stream = stream.filter(p -> p.getAutori() != null &&
                    p.getAutori().stream().anyMatch(a -> a.toLowerCase().contains(al)));
        }
        if (categorie != null && !categorie.isBlank()) {
            String cl = categorie.toLowerCase();
            stream = stream.filter(p -> p.getCategorie() != null &&
                    p.getCategorie().toLowerCase().contains(cl));
        }
        if (anStart != null) stream = stream.filter(p -> p.getAnPublicare() >= anStart);
        if (anEnd   != null) stream = stream.filter(p -> p.getAnPublicare() <= anEnd);

        if (criteriiSortare != null && !criteriiSortare.isEmpty()) {
            Comparator<Publicatie> comp = construiesteComparator(criteriiSortare);
            if (comp != null) stream = stream.sorted(comp);
        }
        return stream.toList();
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
        Cititor c = cititori.get(idCit);
        if (c == null) throw new EntitateInexistentaExceptie("Cititor", idCit);

        imprumutChain.validate(p, c);

        p.imprumuta();
        Imprumut impr = new Imprumut(idPub, idCit);
        c.adaugaImprumutActiv(impr);
        imprumuturi.add(impr);

        EventBus.publish(AuditAction.PUBLICATIE_IMPRUMUTATA);
    }

    public void returneazaPublicatie(int idPub, int idCit) throws EntitateInexistentaExceptie {
        Publicatie p = getPublicatieById(idPub);
        Cititor c = cititori.get(idCit);
        if (c == null) throw new EntitateInexistentaExceptie("Cititor", idCit);

        Imprumut impr = c.gasesteImprumutActiv(idPub);
        if (impr == null) throw new EntitateInexistentaExceptie("Imprumut", idPub);

        p.returneaza();
        impr.setDataReturnare(LocalDate.now());

        long zile = ChronoUnit.DAYS.between(impr.getDataScadenta(), impr.getDataReturnare());
        if (zile > 0) {
            double pen = p.calcPenalizare(zile);
            impr.setPenalitate(pen);
            c.adaugaPenalizare(pen);
        }

        c.stergeImprumutActiv(impr);
        c.adaugaInIstoric(impr);
        proceseazaRezervareLaReturnare(idPub);

        EventBus.publish(AuditAction.PUBLICATIE_RETURNATA);
    }

    public void reinnoiesteImprumut(int idPub, int idCit) throws Exception {
        Publicatie p = getPublicatieById(idPub);
        Cititor c = cititori.get(idCit);
        if (c == null) throw new EntitateInexistentaExceptie("Cititor", idCit);

        reinnoireChain.validate(p, c);

        Imprumut impr = c.gasesteImprumutActiv(idPub);
        impr.setDataScadenta(impr.getDataScadenta().plusDays(p.durataMaximaZile()));
        impr.setNumarReinnoiri(impr.getNumarReinnoiri() + 1);

        EventBus.publish(AuditAction.IMPRUMUT_REINNOIT);
    }

    private void proceseazaRezervareLaReturnare(int idPub) {
        RezervarePublicatie rez = rezervari.get(idPub);
        if (rez == null || rez.esteGoala()) return;

        Cititor urmator = rez.extrageUrmatorul();
        if (rez.esteGoala()) rezervari.remove(idPub);

        try {
            imprumutaPublicatie(idPub, urmator.getId());
        } catch (Exception e) {
            proceseazaRezervareLaReturnare(idPub);
        }
    }

    public void adaugaRezervare(int idPub) {
        rezervari.putIfAbsent(idPub, new RezervarePublicatie(idPub));
        EventBus.publish(AuditAction.REZERVARE_ADAUGATA);
    }

    public void rezervaPublicatie(int idPub, int idCit) throws Exception {
        Publicatie p = getPublicatieById(idPub);
        Cititor   c = cititori.get(idCit);
        if (c == null) throw new EntitateInexistentaExceptie("Cititor", idCit);

        rezervareChain.validate(p, c);

        RezervarePublicatie rez = rezervari.computeIfAbsent(
                idPub,
                k -> new RezervarePublicatie(idPub)
        );
        rez.adaugaInCoada(c);

        EventBus.publish(AuditAction.PUBLICATIE_REZERVATA);
    }

    public void adaugaRecenzie(int idPub, Recenzie r) throws EntitateInexistentaExceptie {
        getPublicatieById(idPub).getListaRecenzii().add(r);
        getPublicatieById(idPub).updateRating();
        EventBus.publish(AuditAction.RECENZIE_ADAUGATA);
    }

    public double calculeazaRatingMediu(int idPub) throws EntitateInexistentaExceptie {
        return getPublicatieById(idPub).getRating();
    }

    public void adaugaCititor(Cititor c) {
        cititori.put(c.getId(), c);
        EventBus.publish(AuditAction.CITITOR_ADAUGAT);
    }

    public void adaugaBibliotecar(Bibliotecar b) {
        bibliotecari.put(b.getId(), b);
        EventBus.publish(AuditAction.BIBLIOTECAR_ADAUGAT);
    }

    public Map<Integer, Cititor> getCititoriMap() {
        return Collections.unmodifiableMap(cititori);
    }

    public Map<Integer, Bibliotecar> getBibliotecariMap() {
        return Collections.unmodifiableMap(bibliotecari);
    }

    public void blocheazaUtilizator(int id) throws AccesInterzisExceptie, EntitateInexistentaExceptie {
        AuthService.getInstance().verificaBibliotecar(RolBibliotecar.ADMIN);
        cautaPersoana(id).setBlocat(false);
        EventBus.publish(AuditAction.UTILIZATOR_BLOCAT);
    }

    public void deblocheazaUtilizator(int id) throws AccesInterzisExceptie, EntitateInexistentaExceptie {
        AuthService.getInstance().verificaBibliotecar(RolBibliotecar.ADMIN);
        cautaPersoana(id).setBlocat(true);
        EventBus.publish(AuditAction.UTILIZATOR_DEBLOCAT);
    }

    public void adaugaBibliotecarStaff(Bibliotecar b) throws AccesInterzisExceptie {
        AuthService.getInstance().verificaBibliotecar(RolBibliotecar.ADMIN);
        b.setRol(RolBibliotecar.STAFF);
        bibliotecari.put(b.getId(), b);
        EventBus.publish(AuditAction.BIBLIOTECAR_STAFF_CREAT);
    }

    private Persoana cautaPersoana(int id) throws EntitateInexistentaExceptie {
        Persoana p = cititori.get(id);
        if (p == null) p = bibliotecari.get(id);
        if (p == null) throw new EntitateInexistentaExceptie("Persoana", id);
        return p;
    }
}
