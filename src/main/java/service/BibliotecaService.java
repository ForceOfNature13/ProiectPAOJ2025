package service;

import exceptie.*;
import model.*;
import util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

public class BibliotecaService {

    private static BibliotecaService instance;

    private final Map<Integer, Publicatie> publicatii = new HashMap<>();
    private final Map<Integer, Cititor> cititori = new HashMap<>();
    private final Map<Integer, Bibliotecar> bibliotecari = new HashMap<>();
    private final List<Imprumut> imprumuturi = new ArrayList<>();
    private final Map<Integer, RezervarePublicatie> rezervari = new HashMap<>();

    private static final double PENALIZARE_PE_ZI = 1.5;
    private static final int MAX_REINNOIRI = 2;

    private BibliotecaService() {}

    public static BibliotecaService getInstance() {
        if (instance == null) instance = new BibliotecaService();
        return instance;
    }

    public void adaugaPublicatie(Publicatie p) {
        publicatii.put(p.getId(), p);
    }

    public void stergePublicatie(int id) {
        publicatii.remove(id);
    }

    public Publicatie getPublicatieById(int id) throws EntitateInexistentaExceptie {
        Publicatie p = publicatii.get(id);
        if (p == null) throw new EntitateInexistentaExceptie("Publicatie", id);
        return p;
    }

    public List<Publicatie> getToatePublicatiile() { return new ArrayList<>(publicatii.values()); }

    public List<Publicatie> cautaDupaTitlu(String t) {
        return publicatii.values().stream()
                .filter(p -> p.getTitlu() != null && p.getTitlu().toLowerCase().contains(t.toLowerCase()))
                .toList();
    }

    public List<Publicatie> cautaDupaCategorie(String c) {
        return publicatii.values().stream()
                .filter(p -> p.getCategorie() != null && p.getCategorie().equalsIgnoreCase(c))
                .toList();
    }

    public List<Publicatie> cautaDupaIntervalAni(int s, int e) {
        return publicatii.values().stream()
                .filter(p -> p.getAnPublicare() >= s && p.getAnPublicare() <= e)
                .toList();
    }
    public List<Publicatie> cautaDupaDisponibilitate(boolean disponibil) {
        return publicatii.values().stream()
                .filter(p -> p.isDisponibil() == disponibil)
                .toList();
    }



    public List<Publicatie> cautareComplexa(String titlu,
                                            String autor,
                                            String categorie,
                                            Integer anStart,
                                            Integer anEnd,
                                            List<String> criteriiSortare) {

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

            Map<String, Comparator<Publicatie>> map = Map.of(
                    "an",            new ComparatorPublicatieDupaAn(),
                    "rating",        new ComparatorPublicatieDupaRating(),
                    "nrimprumuturi", new ComparatorPublicatieDupaNrImprumuturi(),
                    "titlu",         new ComparatorPublicatieDupaTitlu()
            );

            Comparator<Publicatie> comp = null;
            for (String crt : criteriiSortare) {
                Comparator<Publicatie> add = map.get(crt.toLowerCase());
                if (add != null) {
                    comp = (comp == null) ? add : comp.thenComparing(add);
                }
            }
            if (comp != null) stream = stream.sorted(comp);
        }

        return stream.toList();
    }

    public List<Publicatie> sorteazaDupaAn() {
        return publicatii.values().stream()
                .sorted(new ComparatorPublicatieDupaAn())
                .toList();
    }

    public List<Publicatie> sorteazaDupaRating() {
        return publicatii.values().stream()
                .sorted(new ComparatorPublicatieDupaRating())
                .toList();
    }

    public List<Publicatie> sorteazaDupaNrImprumuturi() {
        return publicatii.values().stream()
                .sorted(new ComparatorPublicatieDupaNrImprumuturi())
                .toList();
    }

    public List<Publicatie> sorteazaDupaTitlu() {
        return publicatii.values().stream()
                .sorted(new ComparatorPublicatieDupaTitlu())
                .toList();
    }

    public void imprumutaPublicatie(int idPub, int idCit)
            throws EntitateInexistentaExceptie,
            ResursaIndisponibilaExceptie,
            LimitaDepasitaExceptie {

        Publicatie p = getPublicatieById(idPub);
        Cititor c   = cititori.get(idCit);
        if (c == null) throw new EntitateInexistentaExceptie("Cititor", idCit);

        if (c.getListaImprumuturiActive().size() >= c.getNrMaxImprumuturi()) {
            throw new LimitaDepasitaExceptie(
                    TipLimita.IMPRUMUTURI_ACTIVE,
                    c.getListaImprumuturiActive().size(),
                    c.getNrMaxImprumuturi());
        }

        p.imprumuta();
        Imprumut impr = new Imprumut(idPub, idCit);
        c.adaugaImprumutActiv(impr);
        imprumuturi.add(impr);
    }

    public void returneazaPublicatie(int idPub, int idCit)
            throws EntitateInexistentaExceptie {

        Publicatie p = getPublicatieById(idPub);
        Cititor c   = cititori.get(idCit);
        if (c == null) throw new EntitateInexistentaExceptie("Cititor", idCit);

        Imprumut impr = c.gasesteImprumutActiv(idPub);
        if (impr == null) throw new EntitateInexistentaExceptie("Imprumut", idPub);

        p.returneaza();
        impr.setDataReturnare(LocalDate.now());
        long zile = ChronoUnit.DAYS.between(impr.getDataScadenta(), impr.getDataReturnare());
        if (zile > 0) {
            double pen = zile * PENALIZARE_PE_ZI;
            impr.setPenalitate(pen);
            c.adaugaPenalizare(pen);
        }
        c.stergeImprumutActiv(impr);
        c.adaugaInIstoric(impr);
    }

    public void reinnoiesteImprumut(int idPub, int idCit)
            throws EntitateInexistentaExceptie,
            ResursaIndisponibilaExceptie,
            LimitaDepasitaExceptie {

        Publicatie p = getPublicatieById(idPub);
        Cititor c   = cititori.get(idCit);
        if (c == null) throw new EntitateInexistentaExceptie("Cititor", idCit);

        Imprumut impr = c.gasesteImprumutActiv(idPub);
        if (impr == null) throw new EntitateInexistentaExceptie("Imprumut", idPub);

        if (impr.getNumarReinnoiri() >= MAX_REINNOIRI) {
            throw new LimitaDepasitaExceptie(
                    TipLimita.REINNOIRI,
                    impr.getNumarReinnoiri(), MAX_REINNOIRI);
        }

        RezervarePublicatie rez = rezervari.get(idPub);
        if (rez != null && !rez.esteGoala()) {
            Cititor primul = rez.vizualizeazaUrmatorul();
            if (primul != null && primul.getId() != idCit) {
                throw new ResursaIndisponibilaExceptie(
                        MotivIndisponibilitate.REZERVATA, idPub);
            }
        }

        impr.setDataScadenta(impr.getDataScadenta().plusDays(p.durataMaximaZile()));
        impr.setNumarReinnoiri(impr.getNumarReinnoiri() + 1);
    }

    public void adaugaRezervare(int idPub) {
        rezervari.putIfAbsent(idPub, new RezervarePublicatie(idPub, 5));
    }

    public void rezervaPublicatie(int idPub, int idCit)
            throws EntitateInexistentaExceptie,
            LimitaDepasitaExceptie {

        RezervarePublicatie rez = rezervari.get(idPub);
        if (rez == null) throw new EntitateInexistentaExceptie("Rezervare", idPub);

        Cititor c = cititori.get(idCit);
        if (c == null) throw new EntitateInexistentaExceptie("Cititor", idCit);

        rez.adaugaInCoada(c);
    }

    public void adaugaRecenzie(int idPub, Recenzie r) throws EntitateInexistentaExceptie {
        getPublicatieById(idPub).getListaRecenzii().add(r);
    }

    public double calculeazaRatingMediu(int idPub) throws EntitateInexistentaExceptie {
        Publicatie p = getPublicatieById(idPub);
        if (p.getListaRecenzii().isEmpty()) return 0;
        return p.getListaRecenzii().stream().mapToInt(Recenzie::getRating).average().orElse(0);
    }

    public void adaugaCititor(Cititor c) {
        cititori.put(c.getId(), c);
    }
    public void adaugaBibliotecar(Bibliotecar b) {
        bibliotecari.put(b.getId(), b); }

    public Map<Integer, Cititor> getCititoriMap() {
            return Collections.unmodifiableMap(cititori);
    }

    public Map<Integer, Bibliotecar> getBibliotecariMap() {
        return Collections.unmodifiableMap(bibliotecari);
    }
    public Map<Integer, RezervarePublicatie> getRezervariMap() {
        return Collections.unmodifiableMap(rezervari);
    }
    public List<Imprumut> getImprumuturi() {
        return Collections.unmodifiableList(imprumuturi);
    }
}
