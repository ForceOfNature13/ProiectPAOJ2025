## Definirea sistemului – SmartLibrary

### 📋 Actiuni si interogari:

1. 📚 Listare toate publicatiile  
   Afiseaza fiecare titlu din colectie.

2. 🔍 Cautare publicatie dupa titlu  
   Filtreaza dupa sir introdus de utilizator.

3. 🖋️ Cautare publicatie dupa autor  
   Returneaza titlurile scrise de un autor dat.

4. 🗂️ Cautare publicatie dupa categorie  
   Afiseaza toate publicatiile dintr‑o categorie.

5. ⏳ Cautare publicatie dupa interval de ani  
   Selecteaza titlurile publicate intre doi ani indicati.

6. 🚦 Cautare publicatie dupa disponibilitate  
   Listeaza doar titlurile libere ori cele imprumutate.

7. 🧮 Cautare complexa cu sortare multipla  
   Combina filtre si ordoneaza dupa an, rating, numar de imprumuturi sau titlu.

8. 🗓️ Sortare dupa anul publicarii  
   Ordoneaza descrescator toate publicatiile.

9. ⭐ Sortare dupa rating mediu  
   Ordoneaza descrescator pe baza mediilor recenziilor.

10. 🔢 Sortare dupa numar de imprumuturi  
    Ordoneaza descrescator dupa popularitate.

11. 🔠 Sortare dupa titlu  
    Ordoneaza alfabetic toate publicatiile.

12. 📥 Imprumuta publicatie  
    Creeaza un obiect Imprumut si marcheaza titlul ca indisponibil.

13. 📤 Returneaza publicatie  
    Finalizeaza imprumutul, elibereaza titlul si calculeaza penalizari.

14. ⏳ Rezerva publicatie  
    Adauga cititorul in coada de asteptare a publicatiei.

15. 🔄 Reinnoieste imprumut  
    Prelungeste data scadenta cu o perioada suplimentara.

16. 📝 Adauga recenzie la publicatie  
    Salveaza un rating de la unu la cinci si un comentariu.

17. 👀 Afiseaza recenziile unei publicatii  
    Listeaza toate recenziile si calculeaza ratingul mediu.

18. 📅 Listare evenimente  
    Afiseaza toate evenimentele literare programate.

19. 🏷️ Inscriere la eveniment  
    Adauga cititorul in lista participantilor.

20. 📑 Vizualizare imprumuturi active  
    Afiseaza imprumuturile curente ale cititorului.

21. 🗄️ Vizualizare istoric imprumuturi  
    Afiseaza imprumuturile finalizate.

22. 💰 Vizualizare penalizari  
    Afiseaza suma totala a amenzilor datorate.

23. ➕ Adauga publicatie  
    Bibliotecarul creeaza o carte, revista sau audiobook nou.

24. ❌ Sterge publicatie  
    Bibliotecarul elimina o publicatie dupa ID.

25. ✨ Creeaza eveniment  
    Administratorul adauga un eveniment nou in calendar.

26. 🗑️ Sterge eveniment  
    Administratorul elimina un eveniment existent.

27. 🆕 Inregistrare cititor  
    Salveaza datele si credentialele unui cititor nou.

28. 🔐 Autentificare  
    Valideaza username si parola si seteaza utilizatorul curent.

29. 🚪 Deconectare  
    Sterge sesiunea utilizatorului curent.

---

### 🧩 Tipuri de obiecte:

- 📗 Carte – publicatie tiparita identificata prin ISBN si editura.
- 📰 Revista – publicatie periodica definita prin frecventa si numar.
- 🎧 Audiobook – publicatie audio descrisa prin durata, naratori si format.
- 🏷️ Publicatie – clasa abstracta cu atribute comune tuturor titlurilor.
- 🙋 Cititor – utilizator final cu imprumuturi, penalizari si recenzii.
- 👩‍💼 Bibliotecar – angajat al bibliotecii cu rol STAFF sau ADMIN.
- 🔄 Imprumut – legatura intre un cititor si o publicatie, cu date de imprumut si returnare.
- ⏳ RezervarePublicatie – coada de cititori care asteapta un titlu indisponibil.
- 📆 Eveniment – activitate culturala cu data, locatie si capacitate maxima.
- ⭐ Recenzie – evaluare numerica si comentariu pentru o publicatie.
- 🏢 Editura – entitate cu nume si tara editurii unei carti.
- 🧑 Persoana – superclasa abstracta pentru Cititor si Bibliotecar.
- 🛡️ RolBibliotecar – enumeratie cu valorile STAFF si ADMIN.
- 🔑 AuthService – serviciu singleton pentru autentificare si sesiuni.

---

### 🛠️ Structuri de date si concepte cheie:

- 📂 Colections: ArrayList, HashMap, TreeSet, LinkedList (Queue).
- 🚀 Stream API: filter, map, sorted pentru cautari si sortari fluente.
- 🧩 Comparatoare: clase dedicate pentru an, rating, numar de imprumuturi, titlu.
- 📝 Record: Editura este definita ca record imutabil.
- 🏷️ Enum: RolBibliotecar pentru controlul drepturilor.
- 🏛️ Mostenire: Publicatie si Persoana sunt clase abstracte reutilizate.
- 🔗 Interfata: Imprumutabil pentru polimorfism la imprumut si returnare.
- ♾️ Singleton: serviciile BibliotecaService, EvenimentService, AuthService.
- ⚠️ Exceptii personalizate: AccesInterzis, ResursaIndisponibila, LimitaDepasita etc.
- ⏰ Tipuri de data java.time: LocalDate si LocalDateTime pentru imprumuturi si evenimente.
- 🆔 Generatoare de ID statice: in fiecare entitate pentru identificare unica.
