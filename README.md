<div class="smartlib-container">
  <h1 class="smartlib-hero-title">🏛️ SmartLibrary</h1>
  <p class="smartlib-subtitle">Soluție modernă pentru gestionarea inteligentă a bibliotecilor – Java 21, arhitectură scalabilă.</p>

  <details open>
    <summary>📌 Detalii proiect</summary>
    <p>📖 SmartLibrary revoluționează activitatea bibliotecilor prin catalogare avansată, împrumuturi instant, cozi de rezervare optimizate și evenimente culturale integrate.</p>
    <ul>
      <li>📚 <strong>29 CLI</strong> – comenzi complete pentru operațiuni zilnice</li>
      <li>☕ <strong>Java 21</strong> – Records, switch-expressions, Stream API</li>
      <li>🛡️ <strong>Audit Async</strong> – EventBus → AuditService (thread dedicat, LinkedBlockingQueue)</li>
      <li>🏫 <strong>User-centric</strong> – UI intuitivă, cozi FIFO de rezervare, penalizări automate, roluri STAFF/ADMIN</li>
    </ul>
  </details>

  <details>
    <summary>📋 Comenzi & Interogări</summary>
    <ol>
      <li>📚 Listare publicații</li>
      <li>🔍 Căutare după titlu</li>
      <li>✍️ Căutare după autor</li>
      <li>🗂️ Căutare după categorie</li>
      <li>⏳ Căutare după interval de ani</li>
      <li>🚦 Căutare după disponibilitate</li>
      <li>🧮 Căutare complexă + sortare multiplă</li>
      <li>🗓️ Sortare după anul publicării</li>
      <li>⭐ Sortare după rating</li>
      <li>🔢 Sortare după nr. de împrumuturi</li>
      <li>🔠 Sortare alfabetică</li>
      <li>📥 Împrumută publicație</li>
      <li>📤 Returnează publicație + penalizare</li>
      <li>📌 Rezervă publicație (FIFO)</li>
      <li>🔄 Reînnoiește împrumut</li>
      <li>📝 Adaugă recenzie</li>
      <li>👀 Vezi recenzii + rating mediu</li>
      <li>📅 Listare evenimente</li>
      <li>🏷️ Înscriere la eveniment</li>
      <li>📑 Vizualizare împrumuturi active</li>
      <li>📂 Istoric împrumuturi</li>
      <li>💰 Vizualizare penalizări</li>
      <li>➕ Adaugă publicație</li>
      <li>❌ Șterge publicație</li>
      <li>✨ Creează eveniment</li>
      <li>🗑️ Șterge eveniment</li>
      <li>🔒 Blochează utilizator</li>
      <li>🔓 Deblochează utilizator</li>
      <li>👩‍💼 Adaugă bibliotecar STAFF</li>
    </ol>
  </details>

  <details>
    <summary>🧩 Model</summary>
    <table>
      <thead>
        <tr><th>Entitate</th><th>Descriere</th></tr>
      </thead>
      <tbody>
        <tr><td>📗 Carte</td><td>ISBN · Editură · Categorie</td></tr>
        <tr><td>📰 Revistă</td><td>Frecvență · Număr</td></tr>
        <tr><td>🎧 Audiobook</td><td>Durată · Naratori · Format</td></tr>
        <tr><td>🏷️ Publicație</td><td>Abstractă, atribute comune</td></tr>
        <tr><td>🙋 Cititor</td><td>Împrumuturi · Recenzii · Penalizări</td></tr>
        <tr><td>👩‍💼 Bibliotecar</td><td>Rol STAFF / ADMIN</td></tr>
        <tr><td>🔄 Împrumut</td><td>Date împrumut / returnare</td></tr>
        <tr><td>⏳ RezervarePublicatie</td><td>Coadă FIFO</td></tr>
        <tr><td>📆 Eveniment</td><td>Dată · Locație · Capacitate</td></tr>
        <tr><td>⭐ Recenzie</td><td>Rating ★ + Comentariu</td></tr>
        <tr><td>🏢 Editura</td><td>Record imutabil (`record`)</td></tr>
        <tr><td>🧑 Persoană</td><td>Abstractă (bază utilizatori)</td></tr>
        <tr><td>🛡️ RolBibliotecar</td><td>Enum STAFF, ADMIN</td></tr>
      </tbody>
    </table>
  </details>

  <details>
    <summary>🔧 Arhitectură & Concepte</summary>
    <ul>
      <li>🔸 <strong>POO</strong> – Moștenire, Polimorfism, Încapsulare prin clase abstracte și interfețe (Imprumutabil)</li>
      <li>🔸 <strong>Java 21</strong> – record pentru Editura, enum pentru RolBibliotecar, Stream API (filter, map, sorted)</li>
      <li>🔸 <strong>Colecții</strong> – ArrayList, HashMap, TreeSet, LinkedList pentru stocare și cozi FIFO</li>
      <li>🔸 <strong>Excepții personalizate</strong> – AccesInterzisExceptie, ResursaIndisponibilaExceptie, LimitaDepasitaExceptie</li>
      <li>🔹 <strong>Singleton</strong> – serviciile principale (AuthService, BibliotecaService, AuditService)</li>
      <li>🔹 <strong>Factory</strong> – instanțierea publicațiilor (CarteFactory, RevistaFactory, AudiobookFactory)</li>
      <li>🔹 <strong>Builder</strong> – pentru obiecte complexe (CititorBuilder, EvenimentBuilder)</li>
      <li>🔹 <strong>Strategy</strong> – sortări dinamice prin SortContext și comparatoare</li>
      <li>🔹 <strong>Chain of Responsibility</strong> – validare lanț (LimitaImprumuturiHandler → CoadaRezervariPlinaHandler)</li>
      <li>🔹 <strong>Observer</strong> – audit asincron (EventBus → AuditService)</li>
      <li>🔹 <strong>DTO</strong> – PublicatieDTO pentru transfer de date între straturi</li>
    </ul>
  </details>
</div>