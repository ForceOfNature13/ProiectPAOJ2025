package main;

import audit.*;
import exceptie.*;
import factory.*;
import model.*;
import service.AuthService;
import service.BibliotecaService;
import service.EdituraServiceCrud;
import service.EvenimentService;
import util.CreeazaTabele;
import util.ParolaUtil;
import util.StergeTabele;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        StergeTabele.stergeSchema();
        CreeazaTabele.creazaSchema();

        AuditService.initObserver();

        BibliotecaService bibliotecaService = BibliotecaService.getInstance();
        EvenimentService.getInstance().preloadCache();
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
                case "1" -> doLogin();
                case "2" -> doRegister(bibliotecaService);
                case "3" -> {
                    System.out.println("La revedere!");
                    running = false;
                }
                default -> System.out.println("Optiune invalida!");
            }
        }
    }

    private static void doLogin() {

        AuthService auth = AuthService.getInstance();

        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Parola: ");
        String pass = scanner.nextLine();

        try {
            auth.login(user, pass);
        } catch (AutentificareExceptie e) {
            System.out.println(e.getMessage());
            return;
        }

        Persoana curent = auth.getUtilizatorCurent();
        if (curent instanceof Cititor) meniuCititor();
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
        String parolaHashed = ParolaUtil.hash(scanner.nextLine());
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
                    .parola(parolaHashed)
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
            System.out.println("15. Reinoieste imprumut");
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
                case "15" -> reinoiesteImprumut();
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
            System.out.println("12. Afiseaza recenziile unui cititor");
            System.out.println("13. Afiseaza recenziile unei publicatii");
            System.out.println("14. Listare evenimente");
            System.out.println("15. Vizualizare imprumuturi active ale unui cititor");
            System.out.println("16. Vizualizare istoric imprumuturi ale unui cititor");
            System.out.println("17. Vizualizare amenzi unui cititor");
            System.out.println("18. Adauga publicatie");
            System.out.println("19. Sterge publicatie");
            System.out.println("20. Creeaza eveniment");
            System.out.println("21. Sterge eveniment");
            System.out.println("22. Blocheaza utilizator");
            System.out.println("23. Deblocheaza utilizator");
            System.out.println("24. Adauga bibliotecar STAFF");
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
                case "12" -> afiseazaRecenziiUtilizator();
                case "13" -> afiseazaRecenziiPublicatie();
                case "14" ->  listareEvenimente();
                case "15" -> imprumuturiActiveCititor();
                case "16" -> istoricImprumuturiCititor();
                case "17" -> amenziCititor();
                case "18" -> adaugaPublicatieBibliotecar();
                case "19" -> stergePublicatieBibliotecar();
                case "20" -> creeazaEvenimentBibliotecar();
                case "21" -> stergeEvenimentBibliotecar();
                case "22" -> blocheazaUtilizator();
                case "23" -> deblocheazaUtilizator();
                case "24" -> adaugaBibliotecarStaff();
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

        try {
            var lista = svc.getToatePublicatiile();

            if (lista.isEmpty()) {
                throw new EntitateInexistentaExceptie("Publicatii", -1);
            }

            lista.forEach(System.out::println);
        }
        catch (EntitateInexistentaExceptie ex) {
            System.out.println("Nu exista publicatii in sistem.");
        }
    }

    private static void cautareTitlu() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("Introdu titlul: ");
        String titlu = scanner.nextLine().trim();

        if (titlu.isEmpty()) {
            System.out.println("Introduceti titlul pe care il cautati.");
            return;
        }

        var rezultate = svc.cautaDupaTitlu(titlu);
        if (rezultate.isEmpty()) {
            System.out.println("Nu exista nicio publicatie cu titlul \"" + titlu + "\".");
        } else {
            rezultate.forEach(System.out::println);
        }
    }

    private static void cautareAutor() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("Introdu autorul: ");
        String autor = scanner.nextLine().trim();

        if (autor.isEmpty()) {
            System.out.println("Introduceti autorul pe care il cautati.");
            return;
        }

        var rezultate = svc.cautareComplexa(null, autor, null, null, null, null);
        if (rezultate.isEmpty()) {
            System.out.println("Nu exista publicatii scrise de \"" + autor + "\".");
        } else {
            rezultate.forEach(System.out::println);
        }
    }

    private static void cautareCategorie() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("Introdu categoria: ");
        String cat = scanner.nextLine().trim();

        if (cat.isEmpty()) {
            System.out.println("Introduceti categoria pe care o cautati.");
            return;
        }

        var rezultate = svc.cautaDupaCategorie(cat);
        if (rezultate.isEmpty()) {
            System.out.println("Nu exista publicatii in categoria \"" + cat + "\".");
        } else {
            rezultate.forEach(System.out::println);
        }
    }

    private static void cautareIntervalAni() {
        BibliotecaService svc = BibliotecaService.getInstance();
        try {
            System.out.print("An inceput: ");
            int start = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("An sfarsit: ");
            int end   = Integer.parseInt(scanner.nextLine().trim());

            if (start <= 0 || end <= 0) {
                System.out.println("Anii trebuie sa fie pozitivi.");
                return;
            }
            if (end < start) {
                System.out.println("Anul de sfarsit trebuie sa fie mai mare sau egal cu anul de inceput.");
                return;
            }

            var rezultate = svc.cautaDupaIntervalAni(start, end);
            if (rezultate.isEmpty()) {
                System.out.println("Nu exista publicatii intre anii " + start + " si " + end + ".");
            } else {
                rezultate.forEach(System.out::println);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Te rog introdu numere valide pentru ani.");
        }
    }
    private static void cautareDisponibilitate() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("Disponibil? (true/false): ");
        String input = scanner.nextLine().trim().toLowerCase();

        if (!input.equals("true") && !input.equals("false")) {
            System.out.println("Introdu \"true\" sau \"false\" pentru disponibilitate.");
            return;
        }

        boolean disp = Boolean.parseBoolean(input);
        var rezultate = svc.cautaDupaDisponibilitate(disp);

        if (rezultate.isEmpty()) {
            System.out.println("Nu exista publicatii cu disponibilitatea solicitata.");
        } else {
            rezultate.forEach(System.out::println);
        }
    }
    private static void cautareComplexa() {
        BibliotecaService svc = BibliotecaService.getInstance();

        System.out.print("Titlu (enter pt gol): ");
        String titlu = scanner.nextLine().trim();
        titlu = titlu.isBlank() ? null : titlu;

        System.out.print("Autor (enter pt gol): ");
        String autor = scanner.nextLine().trim();
        autor = autor.isBlank() ? null : autor;

        System.out.print("Categorie (enter pt gol): ");
        String cat = scanner.nextLine().trim();
        cat = cat.isBlank() ? null : cat;

        Integer anStart, anEnd;
        try {
            System.out.print("An inceput (0 pt skip): ");
            int as = Integer.parseInt(scanner.nextLine().trim());
            if (as < 0) {
                System.out.println("Anul trebuie sa fie pozitiv.");
                return;
            }
            anStart = (as == 0 ? null : as);

            System.out.print("An final (0 pt skip): ");
            int af = Integer.parseInt(scanner.nextLine().trim());
            if (af < 0) {
                System.out.println("Anul trebuie sa fie pozitiv.");
                return;
            }
            anEnd = (af == 0 ? null : af);

            if (anStart != null && anEnd != null && anEnd < anStart) {
                System.out.println("Anul de sfarsit trebuie sa fie mai mare sau egal cu anul de inceput.");
                return;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Te rog introdu numere intregi pentru ani.");
            return;
        }

        System.out.print("Criterii sortare (an,rating,nrimprumuturi,titlu): ");
        String critStr = scanner.nextLine();
        List<String> crit = null;
        if (!critStr.isBlank()) {
            Set<String> allowed = Set.of("an", "rating", "nrimprumuturi", "titlu");
            crit = Arrays.stream(critStr.split(","))
                    .map(String::trim)
                    .filter(allowed::contains)
                    .toList();
            if (crit.isEmpty()) {
                System.out.println("Nu ai introdus criterii valide de sortare. Continuam fara sortare.");
                crit = null;
            }
        }

        var rezultate = svc.cautareComplexa(titlu, autor, cat, anStart, anEnd, crit);
        if (rezultate.isEmpty()) {
            System.out.println("Nu au fost gasite publicatii dupa criteriile introduse.");
        } else {
            rezultate.forEach(System.out::println);
        }
    }


    private static void sortareDupaAn() {
        var rez = BibliotecaService.getInstance().sorteazaDupaAn();
        if (rez.isEmpty()) {
            System.out.println("Nu exista publicatii pentru a putea fi sortate dupa an.");
        } else {
            rez.forEach(System.out::println);
        }
    }


    private static void sortareDupaRating() {
        var rez = BibliotecaService.getInstance().sorteazaDupaRating();
        if (rez.isEmpty()) {
            System.out.println("Nu exista publicatii pentru a putea fi sortate dupa rating.");
        } else {
            rez.forEach(System.out::println);
        }
    }


    private static void sortareDupaNrImprumuturi() {
        var rez = BibliotecaService.getInstance().sorteazaDupaNrImprumuturi();
        if (rez.isEmpty()) {
            System.out.println("Nu exista publicatii pentru a putea fi sortate dupa numarul de imprumuturi.");
        } else {
            rez.forEach(System.out::println);
        }
    }


    private static void sortareDupaTitlu() {
        var rez = BibliotecaService.getInstance().sorteazaDupaTitlu();
        if (rez.isEmpty()) {
            System.out.println("Nu exista publicatii pentru a putea fi sortate dupa titlu.");
        } else {
            rez.forEach(System.out::println);
        }
    }


    private static void imprumutaPublicatie() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot imprumuta.");
            return;
        }

        System.out.print("ID publicatie: ");
        String raw = scanner.nextLine().trim();
        int idPub;
        try {
            idPub = Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            System.out.println("Introdu un ID numeric valid.");
            return;
        }

        try {
            svc.imprumutaPublicatie(idPub, cititor.getId());
            System.out.println("Imprumut realizat cu succes.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static void returneazaPublicatie() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot returna.");
            return;
        }

        System.out.print("ID publicatie: ");
        String raw = scanner.nextLine().trim();
        int idPub;
        try {
            idPub = Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            System.out.println("Introdu un ID numeric valid.");
            return;
        }

        try {
            svc.returneazaPublicatie(idPub, cititor.getId());
            System.out.println("Returnare cu succes.");
        } catch (EntitateInexistentaExceptie ex) {
            System.out.println("Publicatia cu ID " + idPub +
                    "este imprumutata de catre tine ca sa o poti returna.");
        } catch (Exception ex) {
            System.out.println("Eroare la returnare: " + ex.getMessage());
        }
    }

    private static void rezervaPublicatie() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot rezerva.");
            return;
        }

        System.out.print("ID publicatie: ");
        String raw = scanner.nextLine().trim();
        int idPub;
        try {
            idPub = Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            System.out.println("Introdu un ID numeric valid.");
            return;
        }

        try {

            svc.rezervaPublicatie(idPub, cititor.getId());
            System.out.println("Rezervarea a fost adaugata cu succes.");
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }


    private static void reinoiesteImprumut() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot reinnoi.");
            return;
        }

        System.out.print("ID publicatie: ");
        String raw = scanner.nextLine().trim();
        int idPub;
        try {
            idPub = Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            System.out.println("Introdu un ID numeric valid.");
            return;
        }

        try {
            svc.reinnoiesteImprumut(idPub, cititor.getId());
            System.out.println("Reinnoire cu succes.");
        }
        catch (EntitateInexistentaExceptie ex) {
            System.out.println("Nu exista un imprumut activ pentru publicatia cu ID "
                    + idPub + " in contul tau.");
        }
        catch (ResursaIndisponibilaExceptie ex) {
            System.out.println("Publicatia cu ID " + idPub
                    + " este rezervata si nu poate fi reinnoita.");
        }
        catch (LimitaDepasitaExceptie ex) {
            System.out.println("Nu poti reinnoi: " + ex.getMessage());
        }
        catch (Exception ex) {
            System.out.println("Eroare la reinnoire: " + ex.getMessage());
        }
    }

    private static void adaugaRecenzie() {
        AuthService auth = AuthService.getInstance();
        BibliotecaService svc = BibliotecaService.getInstance();


        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii pot adauga recenzii.");
            return;
        }


        System.out.print("ID publicatie: ");
        String rawId = scanner.nextLine().trim();
        int idPub;
        try {
            idPub = Integer.parseInt(rawId);
        } catch (NumberFormatException ex) {
            System.out.println("Introdu un ID numeric valid.");
            return;
        }


        try {
            svc.getPublicatieById(idPub);
        } catch (EntitateInexistentaExceptie ex) {
            System.out.println("Publicatia cu ID " + idPub + " nu exista.");
            return;
        }


        System.out.print("Rating (1-5): ");
        String rawRating = scanner.nextLine().trim();
        int rating;
        try {
            rating = Integer.parseInt(rawRating);
        } catch (NumberFormatException ex) {
            System.out.println("Ratingul trebuie sa fie un numar intre 1 si 5.");
            return;
        }
        if (rating < 1 || rating > 5) {
            System.out.println("Ratingul trebuie sa fie intre 1 si 5.");
            return;
        }


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
            try {
                svc.adaugaRecenzie(idPub, rec);
            } catch (EntitateInexistentaExceptie e) {
                throw new RuntimeException(e);
            }
            System.out.println("Recenzie adaugata cu succes.");
        }
        catch (InputInvalidExceptie ex) {
            System.out.println(ex.getMessage());
        }
    }



    private static void afiseazaRecenziiPublicatie() {
        BibliotecaService svc = BibliotecaService.getInstance();

        System.out.print("ID publicatie: ");
        String raw = scanner.nextLine().trim();
        int idPub;
        try {
            idPub = Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            System.out.println("Introdu un ID numeric valid.");
            return;
        }

        try {
            Publicatie p = svc.getPublicatieById(idPub);
            List<Recenzie> recs = p.getListaRecenzii();

            if (recs.isEmpty()) {
                System.out.println("Nu exista recenzii pentru publicatia cu ID " + idPub + ".");
            } else {
                recs.forEach(System.out::println);
                System.out.println("Rating mediu: " + svc.calculeazaRatingMediu(idPub));
            }
        } catch (EntitateInexistentaExceptie ex) {
            System.out.println("Publicatia cu ID " + idPub + " nu exista.");
        }
    }


    private static void inscriereEveniment() {
        AuthService auth = AuthService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Cititor cititor)) {
            System.out.println("Doar cititorii se pot inscrie.");
            return;
        }

        System.out.print("ID eveniment: ");
        String raw = scanner.nextLine().trim();
        int idEv;
        try {
            idEv = Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            System.out.println("Introdu un ID numeric valid.");
            return;
        }

        try {
            EvenimentService.getInstance().inscrieParticipant(idEv, cititor);
            System.out.println("Inscriere realizata cu succes.");
        }
        catch (EntitateInexistentaExceptie ex) {
            System.out.println("Evenimentul cu ID " + idEv + " nu exista.");
        }
        catch (LimitaDepasitaExceptie ex) {
            System.out.println("Nu te poti inscrie: capacitatea maxima a evenimentului a fost atinsa.");
        }
        catch (Exception ex) {
            System.out.println("Eroare la inscriere: " + ex.getMessage());
        }
    }


    private static void vizualizareImprumuturiActive() {
        AuthService auth = AuthService.getInstance();
        if (auth.getUtilizatorCurent() instanceof Cititor cititor) {
            var list = cititor.getListaImprumuturiActive();
            if (list.isEmpty()) {
                System.out.println("Nu aveti imprumuturi active.");
            } else {
                list.forEach(System.out::println);
            }
        } else {
            System.out.println("Doar cititorii au împrumuturi!");
        }
    }


    private static void vizualizareIstoricImprumuturi() {
        AuthService auth = AuthService.getInstance();
        if (auth.getUtilizatorCurent() instanceof Cititor cititor) {
            var ist = cititor.getIstoric();
            if (ist.isEmpty()) {
                System.out.println("Istoricul este gol – niciun Imprumut returnat.");
            } else {
                ist.forEach(System.out::println);
            }
        } else {
            System.out.println("Doar cititorii au istoric!");
        }
    }


    private static void vizualizareAmenzi() {
        AuthService auth = AuthService.getInstance();
        if (auth.getUtilizatorCurent() instanceof Cititor cititor) {
            double suma = cititor.getSumaPenalizari();
            if (suma == 0) {
                System.out.println("Nu aveti amenzi.");
            } else {
                System.out.printf("Total amenzi: %.2f%n", suma);
            }
        } else {
            System.out.println("Doar cititorii pot vedea amenzi!");
        }
    }


    private static void adaugaPublicatieBibliotecar() {
        AuthService auth = AuthService.getInstance();
        if (!(auth.getUtilizatorCurent() instanceof Bibliotecar)) {
            System.out.println("Doar bibliotecarii pot adauga publicatii.");
            return;
        }

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
            if (titlu.isBlank()) {
                System.out.println("Titlul nu poate fi gol.");
                return;
            }

            System.out.print("An publicare: ");
            int an;
            try { an = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException ex) { System.out.println("Anul trebuie sa fie numeric."); return; }
            int curYear = java.time.Year.now().getValue();
            if (an <= 0 || an > curYear) {
                System.out.println("Anul trebuie sa fie pozitiv si cel mult " + curYear + ".");
                return;
            }

            System.out.print("Nr pagini: ");
            int nrPag;
            try { nrPag = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException ex) { System.out.println("Numarul de pagini trebuie sa fie numeric."); return; }
            if (nrPag < 0) { System.out.println("Numarul de pagini nu poate fi negativ."); return; }

            List<String> autori = new ArrayList<>();
            System.out.print("Autori (virgula, enter pt none): ");
            String aIn = scanner.nextLine();
            if (!aIn.isBlank())
                Arrays.stream(aIn.split(",")).map(String::trim).forEach(autori::add);


            Map<String, Object> extra = new HashMap<>();
            PublicatieFactory factory;

            switch (tip) {
                case "1" -> {
                    System.out.print("ISBN: ");
                    extra.put("isbn", scanner.nextLine().trim());


                    Editura edDefault = new Editura(0, "DefaultEditura", "Romania");
                    int newId = EdituraServiceCrud.getInstance().create(edDefault);
                    edDefault = new Editura(newId, edDefault.nume(), edDefault.tara());
                    extra.put("editura", edDefault);

                    System.out.print("Categorie: ");
                    extra.put("categorie", scanner.nextLine().trim());

                    factory = new CarteFactory();
                }
                case "2" -> {
                    System.out.print("Frecventa: ");
                    extra.put("frecventa", scanner.nextLine().trim());

                    System.out.print("Numar: ");
                    int numar;
                    try { numar = Integer.parseInt(scanner.nextLine().trim()); }
                    catch (NumberFormatException ex) { System.out.println("Numarul trebuie sa fie numeric."); return; }
                    extra.put("numar", numar);

                    System.out.print("Categorie: ");
                    extra.put("categorie", scanner.nextLine().trim());

                    factory = new RevistaFactory();
                }
                case "3" -> {
                    System.out.print("Durata (min): ");
                    int durata;
                    try { durata = Integer.parseInt(scanner.nextLine().trim()); }
                    catch (NumberFormatException ex) { System.out.println("Durata trebuie sa fie numerica."); return; }
                    extra.put("durata", durata);

                    System.out.print("Format: ");
                    extra.put("format", scanner.nextLine().trim());

                    System.out.print("Naratori (virgula, enter pt none): ");
                    List<String> nar = new ArrayList<>();
                    String nIn = scanner.nextLine();
                    if (!nIn.isBlank())
                        Arrays.stream(nIn.split(",")).map(String::trim).forEach(nar::add);
                    extra.put("naratori", nar);

                    System.out.print("Categorie: ");
                    extra.put("categorie", scanner.nextLine().trim());

                    factory = new AudiobookFactory();
                }
                default -> {
                    System.out.println("Tip invalid. Alege 1, 2 sau 3.");
                    return;
                }
            }

            PublicatieDTO dto = new PublicatieDTO(titlu, an, nrPag, autori, extra);
            Publicatie p = factory.create(dto);
            svc.adaugaPublicatie(p);

            System.out.println("Publicatie adaugata cu ID " + p.getId());
        }
        catch (Exception ex) {
            System.out.println("Eroare la adaugare: " + ex.getMessage());
        }
    }
    private static void stergePublicatieBibliotecar() {
        AuthService auth = AuthService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Bibliotecar)) {
            System.out.println("Doar bibliotecarii pot sterge publicatii.");
            return;
        }

        BibliotecaService svc = BibliotecaService.getInstance();

        System.out.print("ID publicatie: ");
        String raw = scanner.nextLine().trim();
        int id;
        try {
            id = Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            System.out.println("Introdu un ID numeric valid.");
            return;
        }

        try {
            svc.getPublicatieById(id);
            svc.stergePublicatie(id);
            System.out.println("Publicatia cu ID " + id + " a fost stearsa.");
        } catch (EntitateInexistentaExceptie ex) {
            System.out.println("Publicatia cu ID " + id + " nu exista.");
        } catch (Exception ex) {
            System.out.println("Eroare la stergere: " + ex.getMessage());
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

        if (!(auth.getUtilizatorCurent() instanceof Bibliotecar bib) ||
                bib.getRol() != RolBibliotecar.ADMIN) {
            System.out.println("Nu ai permisiunea de a sterge evenimente.");
            return;
        }
        System.out.print("ID eveniment: ");
        String raw = scanner.nextLine().trim();
        int id;
        try {
            id = Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            System.out.println("Introdu un ID numeric valid.");
            return;
        }

        try {
            EvenimentService.getInstance().stergeEveniment(id);
            System.out.println("Evenimentul cu ID " + id + " a fost sters.");
        }
        catch (EntitateInexistentaExceptie ex) {
            System.out.println("Evenimentul cu ID " + id + " nu exista.");
        }
        catch (AccesInterzisExceptie ex) {
            System.out.println("Nu ai dreptul sa stergi acest eveniment.");
        }
        catch (Exception ex) {
            System.out.println("Eroare la stergere: " + ex.getMessage());
        }
    }



    private static void listareEvenimente() {
        var evs = EvenimentService.getInstance().listareEvenimente();
        if (evs.isEmpty()) {
            System.out.println("Nu exista evenimente programate.");
        } else {
            evs.forEach(System.out::println);
        }
    }

    private static void blocheazaUtilizator() {
        AuthService auth = AuthService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Bibliotecar bib) ||
                bib.getRol() != RolBibliotecar.ADMIN) {
            System.out.println("Doar un bibliotecar ADMIN poate bloca utilizatori.");
            return;
        }

        System.out.print("ID persoana: ");
        String raw = scanner.nextLine().trim();
        int id;
        try { id = Integer.parseInt(raw); }
        catch (NumberFormatException ex) { System.out.println("Introdu un ID numeric valid."); return; }

        try {
            BibliotecaService.getInstance().blocheazaUtilizator(id);
            System.out.println("Utilizatorul cu ID " + id + " a fost blocat.");
        } catch (EntitateInexistentaExceptie ex) {
            System.out.println("Persoana cu ID " + id + " nu exista.");
        } catch (Exception ex) {
            System.out.println("Eroare la blocare: " + ex.getMessage());
        }
    }
    private static void deblocheazaUtilizator() {
        AuthService auth = AuthService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Bibliotecar bib) ||
                bib.getRol() != RolBibliotecar.ADMIN) {
            System.out.println("Doar un bibliotecar ADMIN poate debloca utilizatori.");
            return;
        }

        System.out.print("ID persoana: ");
        String raw = scanner.nextLine().trim();
        int id;
        try { id = Integer.parseInt(raw); }
        catch (NumberFormatException ex) { System.out.println("Introdu un ID numeric valid."); return; }

        try {
            BibliotecaService.getInstance().deblocheazaUtilizator(id);
            System.out.println("Utilizatorul cu ID " + id + " a fost deblocat.");
        } catch (EntitateInexistentaExceptie ex) {
            System.out.println("Persoana cu ID " + id + " nu exista.");
        } catch (Exception ex) {
            System.out.println("Eroare la deblocare: " + ex.getMessage());
        }
    }

    private static void adaugaBibliotecarStaff() {
        AuthService auth = AuthService.getInstance();

        if (!(auth.getUtilizatorCurent() instanceof Bibliotecar bib) ||
                bib.getRol() != RolBibliotecar.ADMIN) {
            System.out.println("Doar un bibliotecar ADMIN poate adauga personal STAFF.");
            return;
        }

        try {
            System.out.print("Nume: ");
            String nume = scanner.nextLine().trim();
            if (nume.isBlank()) { System.out.println("Numele nu poate fi gol."); return; }

            System.out.print("Prenume: ");
            String pren = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (email.isBlank()) { System.out.println("Emailul nu poate fi gol."); return; }

            System.out.print("Telefon: ");
            String tel = scanner.nextLine().trim();

            System.out.print("Username: ");
            String user = scanner.nextLine().trim();
            if (user.isBlank()) { System.out.println("Username-ul nu poate fi gol."); return; }

            System.out.print("Parola: ");
            String passHashed = ParolaUtil.hash(scanner.nextLine());

            Bibliotecar b = new BibliotecarBuilder()
                    .nume(nume)
                    .prenume(pren)
                    .email(email)
                    .telefon(tel)
                    .username(user)
                    .parola(passHashed)
                    .sectie("Sectie necunoscuta")
                    .data(java.time.LocalDate.now())
                    .build();

            BibliotecaService.getInstance().adaugaBibliotecarStaff(b);
            System.out.println("Bibliotecar STAFF adaugat cu ID " + b.getId());
        }
        catch (Exception ex) {
            System.out.println("Eroare la adaugare: " + ex.getMessage());
        }
    }


    private static void afiseazaRecenziiUtilizator() {
        BibliotecaService svc = BibliotecaService.getInstance();

        System.out.print("ID cititor: ");
        int idCit;
        try {
            idCit = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID invalid!");
            return;
        }

        try {
            var recs = svc.getRecenziiCititor(idCit);

            if (recs.isEmpty()) {
                System.out.println("Cititorul nu a scris nicio recenzie.");
            } else {
                recs.forEach(System.out::println);
            }
        } catch (EntitateInexistentaExceptie | AccesInterzisExceptie e) {
            System.out.println(e.getMessage());
        }
    }
    private static void imprumuturiActiveCititor() {
        BibliotecaService svc = BibliotecaService.getInstance();
        System.out.print("ID cititor: ");
        int id;
        try { id = Integer.parseInt(scanner.nextLine()); }
        catch (NumberFormatException e) { System.out.println("ID invalid!"); return; }

        try {
            var loans = svc.getImprumuturiActiveCititor(id);
            if (loans.isEmpty()) {
                System.out.println("Cititorul nu are împrumuturi active.");
            } else {
                loans.forEach(System.out::println);
            }
        } catch (EntitateInexistentaExceptie | AccesInterzisExceptie e) {
            System.out.println(e.getMessage());
        }
    }


    private static void istoricImprumuturiCititor() {
        BibliotecaService svc = BibliotecaService.getInstance();

        System.out.print("ID cititor: ");
        int id;
        try { id = Integer.parseInt(scanner.nextLine()); }
        catch (NumberFormatException e) { System.out.println("ID invalid!"); return; }

        try {
            var loans = svc.getIstoricReturnatCititor(id);

            if (loans.isEmpty()) {
                System.out.println("Cititorul nu are împrumuturi returnate în istoric.");
            } else {
                loans.forEach(System.out::println);
            }
        } catch (EntitateInexistentaExceptie | AccesInterzisExceptie e) {
            System.out.println(e.getMessage());
        }
    }
    private static void amenziCititor() {
        BibliotecaService svc = BibliotecaService.getInstance();

        System.out.print("ID cititor: ");
        int id;
        try { id = Integer.parseInt(scanner.nextLine()); }
        catch (NumberFormatException e) { System.out.println("ID invalid!"); return; }

        try {
            var penalizari = svc.getAmenziCititor(id);

            if (penalizari.isEmpty()) {
                System.out.println("Cititorul nu are amenzi.");
            } else {
                double total = penalizari.stream()
                        .mapToDouble(Imprumut::getPenalitate)
                        .sum();

                System.out.println("Amenzi detaliate:");
                penalizari.forEach(System.out::println);
                System.out.printf("Total amenzi: %.2f%n", total);
            }
        } catch (EntitateInexistentaExceptie | AccesInterzisExceptie e) {
            System.out.println(e.getMessage());
        }
    }



    private static void initData(BibliotecaService svc) {
        if (!svc.getBibliotecariMap().isEmpty()) return;

        Bibliotecar admin = new Bibliotecar(
                "Popescu", "Ion", "ion@example.com", "0712345678",
                "admin",
                ParolaUtil.hash("admin"),
                "Sectie centrala", LocalDate.now());
        admin.setRol(RolBibliotecar.ADMIN);
        svc.adaugaBibliotecar(admin);

        Bibliotecar staff = new Bibliotecar(
                "Ionescu", "Maria", "maria@example.com", "0722345678",
                "maria",
                ParolaUtil.hash("maria123"),
                "Sectie adulti", LocalDate.now());
        staff.setRol(RolBibliotecar.STAFF);
        svc.adaugaBibliotecar(staff);

        Cititor cit = new Cititor(
                "Georgescu", "Ana", "ana@example.com", "0700000000",
                "ana",
                ParolaUtil.hash("pass123"),      
                "Str. Libertatii, 1", 3);
        svc.adaugaCititor(cit);



        Editura edX = new Editura(0, "EdituraX", "Romania");
        int idX = EdituraServiceCrud.getInstance().create(edX);
        edX = new Editura(idX, edX.nume(), edX.tara());

        Carte c = new Carte(
                "Povesti", 2001, 150, true,
                new ArrayList<>(),
                Arrays.asList("Ion Creanga", "Ion Popescu"),
                "ISBN1",
                edX,
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
