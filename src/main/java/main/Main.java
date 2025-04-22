package main;

import audit.*;
import exceptie.*;
import factory.*;
import model.*;
import service.AuthService;
import service.BibliotecaService;
import service.EvenimentService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        AuditService.initObserver();

        BibliotecaService bibliotecaService = BibliotecaService.getInstance();
        initData(bibliotecaService);

        boolean running = true;
        while (running) {
            System.out.println("\n===== MENIU PRINCIPAL =====");
            System.out.println("1. Login");
            System.out.println("2. Register (Cititor nou)");
            System.out.println("3. Exit");
            System.out.print("Alege optiunea: ");
            String opt = scanner.nextLine();

            switch (opt) {
                case "1" -> doLogin(bibliotecaService);
                case "2" -> doRegister(bibliotecaService);
                case "3" -> {
                    System.out.println("La revedere!");
                    running = false;
                }
                default -> System.out.println("Optiune invalida!");
            }
        }
    }

    private static void doLogin(BibliotecaService svc) {

        AuthService auth = AuthService.getInstance();

        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Parola: ");
        String pass = scanner.nextLine();

        try {
            auth.login(user, pass, svc.getCititoriMap(), svc.getBibliotecariMap());
        } catch (AutentificareExceptie e) {
            System.out.println(e.getMessage());
            return;
        }

        Persoana curent = auth.getUtilizatorCurent();
        if (curent instanceof Cititor)      meniuCititor();
        else if (curent instanceof Bibliotecar) meniuBibliotecar();
    }


    private static void doRegister(BibliotecaService bibliotecaService) {
        System.out.print("Nume: ");
        String nume = scanner.nextLine();
        System.out.print("Prenume: ");
        String prenume = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Telefon: ");
        String telefon = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Parola: ");
        String parola = scanner.nextLine();
        System.out.print("Adresa: ");
        String adresa = scanner.nextLine();

        Cititor c;
        try {
            c = new CititorBuilder()
                    .nume(nume)
                    .prenume(prenume)
                    .email(email)
                    .telefon(telefon)
                    .username(username)
                    .parola(parola)
                    .adresa(adresa)
                    .nrMax(3)
                    .build();
        } catch (InputInvalidExceptie e) {
            throw new RuntimeException(e);
        }
        bibliotecaService.adaugaCititor(c);

        System.out.println("Cititor inregistrat cu succes!");
    }

    private static void meniuCititor() {
        AuthService authService = AuthService.getInstance();
        boolean running = true;

        while (running) {
            System.out.println("\n===== MENIU CITITOR =====");
            System.out.println("1. Listare toate publicatiile");
            System.out.println("2. Cautare dupa titlu");
            System.out.println("3. Cautare dupa autor");
            System.out.println("4. Cautare dupa categorie");
            System.out.println("5. Cautare dupa interval de ani");
            System.out.println("6. Cautare dupa disponibilitate");
            System.out.println("7. Cautare complexa");
            System.out.println("8. Sorteaza dupa anul publicarii");
            System.out.println("9. Sorteaza dupa rating");
            System.out.println("10. Sorteaza dupa nr. de imprumuturi");
            System.out.println("11. Sorteaza dupa titlu");
            System.out.println("12. Imprumuta publicatie");
            System.out.println("13. Returneaza publicatie");
            System.out.println("14. Rezerva publicatie");
            System.out.println("15. Reinnoieste imprumut");
            System.out.println("16. Adauga recenzie");
            System.out.println("17. Afiseaza recenzii");
            System.out.println("18. Listare evenimente");
            System.out.println("19. Inscriere la eveniment");
            System.out.println("20. Vizualizare imprumuturi active");
            System.out.println("21. Vizualizare istoric imprumuturi");
            System.out.println("22. Vizualizare amenzi");
            System.out.println("0. Logout");

            System.out.print("Alege optiunea: ");
            String opt = scanner.nextLine();

            switch (opt) {
                case "1" -> listareToatePublicatii();
                case "2" -> cautareTitlu();
                case "3" -> cautareAutor();
                case "4" -> cautareCategorie();
                case "5" -> cautareIntervalAni();
                case "6" -> cautareDisponibilitate();
                case "7" -> cautareComplexa();
                case "8" -> sortareDupaAn();
                case "9" -> sortareDupaRating();
                case "10" -> sortareDupaNrImprumuturi();
                case "11" -> sortareDupaTitlu();
                case "12" -> imprumutaPublicatie();
                case "13" -> returneazaPublicatie();
                case "14" -> rezervaPublicatie();
                case "15" -> reinnoiesteImprumut();
                case "16" -> adaugaRecenzie();
                case "17" -> afiseazaRecenziiPublicatie();
                case "18" -> listareEvenimente();
                case "19" -> inscriereEveniment();
                case "20" -> vizualizareImprumuturiActive();
                case "21" -> vizualizareIstoricImprumuturi();
                case "22" -> vizualizareAmenzi();
                case "0" -> {
                    authService.logout();
                    running = false;
                }
                default -> System.out.println("Optiune invalida!");
            }
        }
    }

    private static void meniuBibliotecar() {
        AuthService authService = AuthService.getInstance();
        boolean running = true;

        while (running) {
            System.out.println("\n===== MENIU BIBLIOTECAR =====");
            System.out.println("== (Contine optiunile cititorului) ==");
            System.out.println("1. Listare toate publicatiile");
            System.out.println("2. Cautare dupa titlu");
            System.out.println("3. Cautare dupa autor");
            System.out.println("4. Cautare dupa categorie");
            System.out.println("5. Cautare dupa interval de ani");
            System.out.println("6. Cautare dupa disponibilitate");
            System.out.println("7. Cautare complexa");
            System.out.println("8. Sorteaza dupa anul publicarii");
            System.out.println("9. Sorteaza dupa rating");
            System.out.println("10. Sorteaza dupa nr. de imprumuturi");
            System.out.println("11. Sorteaza dupa titlu");
            System.out.println("12. Imprumuta publicatie");
            System.out.println("13. Returneaza publicatie");
            System.out.println("14. Rezerva publicatie");
            System.out.println("15. Reinnoieste imprumut");
            System.out.println("16. Adauga recenzie");
            System.out.println("17. Afiseaza recenzii");
            System.out.println("18. Listare evenimente");
            System.out.println("19. Inscriere la eveniment");
            System.out.println("20. Vizualizare imprumuturi active");
            System.out.println("21. Vizualizare istoric imprumuturi");
            System.out.println("22. Vizualizare amenzi");
            System.out.println("== Optiuni administrative ==");
            System.out.println("23. Adauga publicatie");
            System.out.println("24. Sterge publicatie");
            System.out.println("25. Creeaza eveniment");
            System.out.println("26. Sterge eveniment");
            System.out.println("27. Blocheaza utilizator");
            System.out.println("28. Deblocheaza utilizator");
            System.out.println("29. Adauga bibliotecar STAFF");
            System.out.println("0. Logout");

            System.out.print("Alege optiunea: ");
            String opt = scanner.nextLine();

            switch (opt) {
                case "1" -> listareToatePublicatii();
                case "2" -> cautareTitlu();
                case "3" -> cautareAutor();
                case "4" -> cautareCategorie();
                case "5" -> cautareIntervalAni();
                case "6" -> cautareDisponibilitate();
                case "7" -> cautareComplexa();
                case "8" -> sortareDupaAn();
                case "9" -> sortareDupaRating();
                case "10" -> sortareDupaNrImprumuturi();
                case "11" -> sortareDupaTitlu();
                case "12" -> imprumutaPublicatie();
                case "13" -> returneazaPublicatie();
                case "14" -> rezervaPublicatie();
                case "15" -> reinnoiesteImprumut();
                case "16" -> adaugaRecenzie();
                case "17" -> afiseazaRecenziiPublicatie();
                case "18" -> listareEvenimente();
                case "19" -> inscriereEveniment();
                case "20" -> vizualizareImprumuturiActive();
                case "21" -> vizualizareIstoricImprumuturi();
                case "22" -> vizualizareAmenzi();
                case "23" -> adaugaPublicatieBibliotecar();
                case "24" -> stergePublicatieBibliotecar();
                case "25" -> creeazaEvenimentBibliotecar();
                case "26" -> stergeEvenimentBibliotecar();
                case "27" -> blocheazaUtilizator();
                case "28" -> deblocheazaUtilizator();
                case "29" -> adaugaBibliotecarStaff();
                case "0" -> {
                    authService.logout();
                    running = false;
                }
                default -> System.out.println("Optiune invalida!");
            }
        }
    }

    private static void listareToatePublicatii() {
        BibliotecaService svc = BibliotecaService.getInstance();
        svc.getToatePublicatiile().forEach(System.out::println);
    }

    private static void cautareTitlu() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("Introdu titlul: ");
        String titlu = scanner.nextLine();
        svc.cautaDupaTitlu(titlu).forEach(System.out::println);
    }

    private static void cautareAutor() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("Introdu autorul: ");
        String autor = scanner.nextLine();
        svc.cautareComplexa(null, autor, null, null, null, null).forEach(System.out::println);
    }

    private static void cautareCategorie() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("Introdu categoria: ");
        String cat = scanner.nextLine();
        svc.cautaDupaCategorie(cat).forEach(System.out::println);
    }

    private static void cautareIntervalAni() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("An inceput: ");
        int start = Integer.parseInt(scanner.nextLine());
        System.out.print("An sfarsit: ");
        int end = Integer.parseInt(scanner.nextLine());
        svc.cautaDupaIntervalAni(start, end).forEach(System.out::println);
    }

    private static void cautareDisponibilitate() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("Disponibil? (true/false): ");
        boolean disp = Boolean.parseBoolean(scanner.nextLine());
        svc.cautaDupaDisponibilitate(disp).forEach(System.out::println);
    }


    private static void cautareComplexa() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("Titlu (enter pt gol): ");
        String t = scanner.nextLine();
        System.out.print("Autor (enter pt gol): ");
        String a = scanner.nextLine();
        System.out.print("Categorie (enter pt gol): ");
        String c = scanner.nextLine();
        System.out.print("An inceput (0 pt skip): ");
        int as = Integer.parseInt(scanner.nextLine());
        Integer anStart = (as == 0 ? null : as);
        System.out.print("An final (0 pt skip): ");
        int af = Integer.parseInt(scanner.nextLine());
        Integer anEnd = (af == 0 ? null : af);
        System.out.print("Criterii sortare (an,rating,nrimprumuturi,titlu): ");
        String critStr = scanner.nextLine();
        List<String> crit = critStr.isBlank() ? null
                : Arrays.stream(critStr.split(",")).map(String::trim).collect(Collectors.toList());
        svc.cautareComplexa(t, a, c, anStart, anEnd, crit).forEach(System.out::println);
    }

    private static void sortareDupaAn() {
        BibliotecaService.getInstance().sorteazaDupaAn().forEach(System.out::println);
    }

    private static void sortareDupaRating() {
        BibliotecaService.getInstance().sorteazaDupaRating().forEach(System.out::println);
    }

    private static void sortareDupaNrImprumuturi() {
        BibliotecaService.getInstance().sorteazaDupaNrImprumuturi().forEach(System.out::println);
    }

    private static void sortareDupaTitlu() {
        BibliotecaService.getInstance().sorteazaDupaTitlu().forEach(System.out::println);
    }

    private static void imprumutaPublicatie() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot imprumuta!");
            return;
        }

        System.out.print("ID publicatie: ");
        int idPub = Integer.parseInt(scanner.nextLine());
        try {
            svc.imprumutaPublicatie(idPub, cititor.getId());
            System.out.println("Imprumut realizat!");
            /* ===== BLOCAJ TEST PENALIZARE =====
            // forteaza scadenta cu 3 zile in urma pentru a verifica penalizarea

            Imprumut impr = cititor.gasesteImprumutActiv(idPub);
            if (impr != null) {
                impr.setDataScadenta(java.time.LocalDate.now().minusDays(3));
                System.out.println("(DEBUG) Scadenta mutata in trecut cu 3 zile.");
            }*/

            /* ================================== */
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void returneazaPublicatie() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot returna!");
            return;
        }

        System.out.print("ID publicatie: ");
        int idPub = Integer.parseInt(scanner.nextLine());
        try {
            svc.returneazaPublicatie(idPub, cititor.getId());
            System.out.println("Returnare cu succes!");
        } catch (EntitateInexistentaExceptie e) {
            System.out.println(e.getMessage());
        }
    }

    private static void rezervaPublicatie() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot rezerva!");
            return;
        }

        System.out.print("ID publicatie: ");
        int idPub = Integer.parseInt(scanner.nextLine());
        svc.adaugaRezervare(idPub);

        try {
            svc.rezervaPublicatie(idPub, cititor.getId());
            System.out.println("Rezervare adaugata!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void reinnoiesteImprumut() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot reinnoi!");
            return;
        }

        System.out.print("ID publicatie: ");
        int idPub = Integer.parseInt(scanner.nextLine());
        try {
            svc.reinnoiesteImprumut(idPub, cititor.getId());
            System.out.println("Reinnoire cu succes!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void adaugaRecenzie() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot adauga recenzii!");
            return;
        }

        System.out.print("ID publicatie: ");
        int idPub = Integer.parseInt(scanner.nextLine());
        System.out.print("Rating (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());
        System.out.print("Comentariu: ");
        String comentariu = scanner.nextLine();

        try {
            Recenzie rec = new Recenzie(
                    idPub,
                    cititor.getId(),
                    rating,
                    comentariu,
                    java.time.LocalDateTime.now()
            );
            svc.adaugaRecenzie(idPub, rec);
            System.out.println("Recenzie adaugata!");
        } catch (InputInvalidExceptie | EntitateInexistentaExceptie e) {
            System.out.println(e.getMessage());
        }
    }

    private static void afiseazaRecenziiPublicatie() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("ID publicatie: ");
        int idPub = Integer.parseInt(scanner.nextLine());

        try {
            Publicatie p = svc.getPublicatieById(idPub);
            List<Recenzie> recs = p.getListaRecenzii();
            if (recs.isEmpty()) {
                System.out.println("Nu exista recenzii.");
            } else {
                recs.forEach(System.out::println);
                System.out.println("Rating mediu: " + svc.calculeazaRatingMediu(idPub));
            }
        } catch (EntitateInexistentaExceptie e) {
            System.out.println(e.getMessage());
        }
    }


    private static void inscriereEveniment() {
        AuthService auth = AuthService.getInstance();
        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii se pot inscrie!");
            return;
        }

        System.out.print("ID eveniment: ");
        int idEv = Integer.parseInt(scanner.nextLine());

        try {
            EvenimentService.getInstance().inscrieParticipant(idEv, cititor);
            System.out.println("Inscris cu succes!");
        } catch (LimitaDepasitaExceptie |
                 EntitateInexistentaExceptie e) {
            System.out.println(e.getMessage());
        }
    }

    private static void vizualizareImprumuturiActive() {
        AuthService auth = AuthService.getInstance();
        if (auth.getUtilizatorCurent() instanceof Cititor cititor) {
            cititor.getListaImprumuturiActive().forEach(System.out::println);
        } else {
            System.out.println("Doar cititorii au imprumuturi!");
        }
    }

    private static void vizualizareIstoricImprumuturi() {
        AuthService auth = AuthService.getInstance();
        if (auth.getUtilizatorCurent() instanceof Cititor cititor) {
            cititor.getIstoric().forEach(System.out::println);
        } else {
            System.out.println("Doar cititorii au istoric!");
        }
    }

    private static void vizualizareAmenzi() {
        AuthService auth = AuthService.getInstance();
        if (auth.getUtilizatorCurent() instanceof Cititor cititor) {
            System.out.println("Suma penalizari: " + cititor.getSumaPenalizari());
        } else {
            System.out.println("Doar cititorii pot vedea amenzi!");
        }
    }

    private static void adaugaPublicatieBibliotecar() {
        BibliotecaService svc = BibliotecaService.getInstance();

        try {
            System.out.println("=== Adauga Publicatie ===");
            System.out.println("1. Carte");
            System.out.println("2. Revista");
            System.out.println("3. Audiobook");
            System.out.print("Tip: ");
            String tip = scanner.nextLine().trim();

            System.out.print("Titlu: ");
            String titlu = scanner.nextLine().trim();
            if (titlu.isBlank()) throw new InputInvalidExceptie("titlu", titlu);

            System.out.print("An publicare: ");
            int an = Integer.parseInt(scanner.nextLine().trim());
            if (an > java.time.Year.now().getValue())
                throw new InputInvalidExceptie("an publicare", an);

            System.out.print("Nr pagini: ");
            int nrPag = Integer.parseInt(scanner.nextLine().trim());
            if (nrPag < 0) throw new InputInvalidExceptie("nr pagini", nrPag);

            List<String> autori = new ArrayList<>();
            System.out.print("Autori (virgula): ");
            String aIn = scanner.nextLine();
            if (!aIn.isBlank())
                Arrays.stream(aIn.split(",")).map(String::trim).forEach(autori::add);

            Map<String,Object> extra = new HashMap<>();
            PublicatieFactory factory = switch (tip) {
                case "1" -> {
                    System.out.print("ISBN: ");
                    extra.put("isbn", scanner.nextLine().trim());

                    extra.put("editura", new Editura(1,"DefaultEditura","Romania"));
                    System.out.print("Categorie: ");
                    extra.put("categorie", scanner.nextLine().trim());

                    yield new CarteFactory();
                }
                case "2" -> {
                    System.out.print("Frecventa: ");
                    extra.put("frecventa", scanner.nextLine().trim());

                    System.out.print("Numar: ");
                    extra.put("numar", Integer.parseInt(scanner.nextLine().trim()));

                    System.out.print("Categorie: ");
                    extra.put("categorie", scanner.nextLine().trim());

                    yield new RevistaFactory();
                }
                case "3" -> {
                    System.out.print("Durata (min): ");
                    extra.put("durata", Integer.parseInt(scanner.nextLine().trim()));

                    System.out.print("Format: ");
                    extra.put("format", scanner.nextLine().trim());

                    System.out.print("Naratori (virgula): ");
                    List<String> nar = new ArrayList<>();
                    String nIn = scanner.nextLine();
                    if (!nIn.isBlank())
                        Arrays.stream(nIn.split(",")).map(String::trim).forEach(nar::add);
                    extra.put("naratori", nar);

                    System.out.print("Categorie: ");
                    extra.put("categorie", scanner.nextLine().trim());

                    yield new AudiobookFactory();
                }
                default -> throw new InputInvalidExceptie("tip publicatie", tip);
            };
            PublicatieDTO dto = new PublicatieDTO(titlu, an, nrPag, autori, extra);
            Publicatie p = factory.create(dto);

            svc.adaugaPublicatie(p);
            System.out.println("Publicatie adaugata cu ID " + p.getId());

        } catch (NumberFormatException e) {
            System.out.println("Introdu valori numerice valide!");
        } catch (InputInvalidExceptie e) {
            System.out.println(e.getMessage());
        }
    }


    private static void stergePublicatieBibliotecar() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("ID publicatie: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            svc.getPublicatieById(id);
            svc.stergePublicatie(id);
            System.out.println("Publicatie stearsa.");
        } catch (EntitateInexistentaExceptie e) {
            System.out.println(e.getMessage());
        }
    }

    private static void creeazaEvenimentBibliotecar() {
        AuthService auth = AuthService.getInstance();
        if (!(auth.getUtilizatorCurent() instanceof Bibliotecar b) || b.getRol() != RolBibliotecar.ADMIN) {
            System.out.println("Acces interzis!");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            System.out.print("Titlu: ");
            String titlu = scanner.nextLine().trim();
            if (titlu.isBlank()) throw new InputInvalidExceptie("titlu", titlu);

            System.out.print("Descriere: ");
            String descriere = scanner.nextLine().trim();
            if (descriere.isBlank()) throw new InputInvalidExceptie("descriere", descriere);

            System.out.print("Data (dd-MM-yyyy): ");
            String dataStr = scanner.nextLine().trim();
            LocalDate data;
            try {
                data = LocalDate.parse(dataStr, fmt);
            } catch (DateTimeParseException e) {
                throw new InputInvalidExceptie("data", dataStr);
            }
            if (!data.isAfter(LocalDate.now()))
                throw new InputInvalidExceptie("data (trebuie sa fie in viitor)", data);

            System.out.print("Locatie: ");
            String locatie = scanner.nextLine().trim();
            if (locatie.isBlank()) throw new InputInvalidExceptie("locatie", locatie);

            System.out.print("Capacitate max: ");
            int capacitate = Integer.parseInt(scanner.nextLine().trim());
            if (capacitate <= 0) throw new InputInvalidExceptie("capacitate", capacitate);

            Eveniment e = new EvenimentBuilder()
                    .titlu(titlu)
                    .descriere(descriere)
                    .data(data)
                    .locatie(locatie)
                    .capacitate(capacitate)
                    .build();

            EvenimentService.getInstance().creazaEveniment(e);
            System.out.println("Eveniment creat cu ID " + e.getId());

        } catch (NumberFormatException ex) {
            System.out.println("Introduceti un numar intreg valid pentru capacitate!");
        } catch (InputInvalidExceptie | AccesInterzisExceptie ex) {
            System.out.println(ex.getMessage());
        }
    }


    private static void stergeEvenimentBibliotecar() {
        AuthService auth = AuthService.getInstance();
        if (!(auth.getUtilizatorCurent() instanceof Bibliotecar b) || b.getRol() != RolBibliotecar.ADMIN) {
            System.out.println("Acces interzis!");
            return;
        }

        System.out.print("ID eveniment: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            EvenimentService.getInstance().stergeEveniment(id);
            System.out.println("Eveniment sters.");
        } catch (AccesInterzisExceptie | EntitateInexistentaExceptie e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listareEvenimente() {
        EvenimentService.getInstance()
                .listareEvenimente()
                .forEach(System.out::println);
    }

    private static void blocheazaUtilizator() {
        System.out.print("ID persoana: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            BibliotecaService.getInstance().blocheazaUtilizator(id);
            System.out.println("Utilizator blocat.");
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    private static void deblocheazaUtilizator() {
        System.out.print("ID persoana: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            BibliotecaService.getInstance().deblocheazaUtilizator(id);
            System.out.println("Utilizator deblocat.");
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    private static void adaugaBibliotecarStaff() {
        try {
            System.out.print("Nume: ");    String nume = scanner.nextLine();
            System.out.print("Prenume: "); String pren = scanner.nextLine();
            System.out.print("Email: ");   String email = scanner.nextLine();
            System.out.print("Telefon: "); String tel = scanner.nextLine();
            System.out.print("Username: ");String user = scanner.nextLine();
            System.out.print("Parola: ");  String pass = scanner.nextLine();

            Bibliotecar b = new BibliotecarBuilder()
                    .nume(nume)
                    .prenume(pren)
                    .email(email)
                    .telefon(tel)
                    .username(user)
                    .parola(pass)
                    .sectie("Sectie necunoscuta")
                    .data(java.time.LocalDate.now())
                    .build();

            BibliotecaService.getInstance().adaugaBibliotecarStaff(b);
            System.out.println("Bibliotecar STAFF adaugat, ID " + b.getId());
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    private static void initData(BibliotecaService svc) {
        Bibliotecar admin = new Bibliotecar(
                "Popescu", "Ion", "ion@example.com", "0712345678",
                "admin", "admin", "Sectie centrala", LocalDate.now());
        admin.setRol(RolBibliotecar.ADMIN);
        svc.adaugaBibliotecar(admin);

        Bibliotecar staff = new Bibliotecar(
                "Ionescu", "Maria", "maria@example.com", "0722345678",
                "maria", "maria123", "Sectie adulti", LocalDate.now());
        staff.setRol(RolBibliotecar.STAFF);
        svc.adaugaBibliotecar(staff);

        Cititor cit = new Cititor(
                "Georgescu", "Ana", "ana@example.com", "0700000000",
                "ana", "pass123", "Str. Libertatii, 1", 3);
        svc.adaugaCititor(cit);

        Carte c = new Carte(
                "Povesti", 2001, 150, true,
                new ArrayList<>(),
                Arrays.asList("Ion Creanga", "Ion Popescu"),
                "ISBN1",
                new Editura(1, "EdituraX", "Romania"),
                "Copii"
        );
        svc.adaugaPublicatie(c);


        Revista r = new Revista(
                "Revista Tehnica", 2022, 40, true,
                new ArrayList<>(), "Lunar", 10, "Tehnologie",
                Collections.singletonList("Colectiv Redactional"));
        svc.adaugaPublicatie(r);

        Audiobook a = new Audiobook(
                "Audio Lecturi", 2020, 0, true,
                new ArrayList<>(), "Lectura",
                Collections.singletonList("Autor Colectiv"),
                360, Collections.singletonList("Actor1"), "MP3");
        svc.adaugaPublicatie(a);
    }
}
