Definirea sistemului – SmartLibrary

Actiuni si interogari:

1.Listare toate publicatiile – afiseaza fiecare titlu din colectie.

2.Cautare publicatie dupa titlu – filtreaza dupa sir introdus de utilizator.

3.Cautare publicatie dupa autor – returneaza titlurile scrise de un autor dat.

4.Cautare publicatie dupa categorie – afiseaza toate publicatiile dintr‑o categorie.

5.Cautare publicatie dupa interval de ani – selecteaza titlurile publicate intre doi ani indicati.

6.Cautare publicatie dupa disponibilitate – listeaza doar titlurile libere ori cele imprumutate.

7.Cautare complexa cu sortare multipla – combina filtre si ordoneaza dupa an rating numar de imprumuturi sau titlu.

8.Sortare dupa anul publicarii – ordoneaza descrescator toate publicatiile.

9.Sortare dupa rating mediu – ordoneaza descrescator pe baza mediilor recenziilor.

10.Sortare dupa numar de imprumuturi – ordoneaza descrescator dupa popularitate.

11.Sortare dupa titlu – ordoneaza alfabetic toate publicatiile.

12.Imprumuta publicatie – creeaza un obiect Imprumut si marcheaza titlul ca indisponibil.

13.Returneaza publicatie – finalizeaza imprumutul elibereaza titlul si calculeaza penalizari.

14.Rezerva publicatie – adauga cititorul in coada de asteptare a publicatiei.

15.Reinnoieste imprumut – prelungeste data scadenta cu o perioada suplimentara.

16.Adauga recenzie la publicatie – salveaza un rating de la unu la cinci si un comentariu.

17.Afiseaza recenziile unei publicatii – listeaza toate recenziile si calculeaza ratingul mediu.

18.Listare evenimente – afiseaza toate evenimentele literare programate.

19.Inscriere la eveniment – adauga cititorul in lista participantilor.

20.Vizualizare imprumuturi active – afiseaza imprumuturile curente ale cititorului.

21.Vizualizare istoric imprumuturi – afiseaza imprumuturile finalizate.

22.Vizualizare penalizari – arata suma totala a amenzilor datorate.

23.Adauga publicatie – bibliotecarul creeaza o noua:carte,revista sau audiobook.

24.Sterge publicatie – bibliotecarul elimina o publicatie dupa ID.

25.Creeaza eveniment – administratorul adauga un nou eveniment in calendar.

26.Sterge eveniment – administratorul elimina un eveniment existent.

27.Inregistrare cititor – salveaza datele si credentialele unui cititor nou.

28.Autentificare – valideaza username si parola si seteaza utilizatorul curent.

29.Deconectare – sterge sesiunea utilizatorului curent.

Tipuri de obiecte:

1.Carte – publicatie tiparita identificata prin ISBN si editura.

2.Revista – publicatie periodica definita prin frecventa si numar.

3.Audiobook – publicatie audio descrisa prin durata naratori si format. 

4.Publicatie – clasa abstracta cu atribute comune tuturor titlurilor.

5.Cititor – utilizator final care detine imprumuturi penalizari si recenzii.

6.Bibliotecar – angajat al bibliotecii cu rol STAFF sau ADMIN.

7.Imprumut – legatura intre un cititor si o publicatie cu date de imprumut si returnare.

8.RezervarePublicatie – coada de cititori care asteapta o publicatie. 

9.Eveniment – activitate culturala cu data locatie si capacitate maxima. 

10.Recenzie – evaluare numerica si comentariu asociate unei publicatii.

11.Editura – entitate cu nume si tara editurii unei carti. 

12.Persoana – superclasa abstracta pentru Cititor si Bibliotecar.

13.RolBibliotecar – enumeratie cu valorile STAFF si ADMIN.

14.AuthService – serviciu singleton care gestioneaza autentificarea si sesiunile. 

Structuri de date si concepte cheie:

1.Colections folosite: List ArrayList Map HashMap Set TreeSet Queue LinkedList ​BibliotecaService

2.Stream API cu filter map sorted pentru cautari si sortari fluente ​BibliotecaService

3.Comparatoare dedicate pentru an rating numar de imprumuturi titlu ​BibliotecaService

4.Record pentru Editura care ofera imutabilitate compacta ​Editura

5.Enum RolBibliotecar pentru controlul drepturilor de acces ​RolBibliotecar

6.Clase abstracte Publicatie si Persoana pentru mostenire si reutilizare cod

7.Interfata Imprumutabil pentru polimorfism la imprumut returnare durata maxima ​Imprumutabil

8.Clase singleton pentru logica de business BibliotecaService EvenimentService AuthService

9.Exceptii personalizate pentru validari AccesInterzis ResursaIndisponibila LimitaDepasita etc

10.Tipuri de data din java time LocalDate si LocalDateTime folosite la imprumuturi si evenimente

11.Generatoare de ID statice in fiecare entitate pentru identificare unica