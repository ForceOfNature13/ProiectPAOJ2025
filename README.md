<!DOCTYPE html>
<html lang="ro">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SmartLibrary · Prezentare</title>
  <style>
    body { background:#0d1117; color:#c9d1d9; font-family:'Segoe UI',sans-serif; margin:0; padding:2rem; }
    h1 { color:#58a6ff; font-size:2.5rem; margin-bottom:0.5rem; }
    p.subtitle { color:#c9d1d9; font-size:1.1rem; margin-bottom:2rem; }
    details { background:#161b22; padding:1rem; border-radius:0.5rem; margin-bottom:1rem; }
    summary { cursor:pointer; font-size:1.25rem; font-weight:bold; }
    summary::marker { font-size:1.25rem; }
    ul, ol, table { margin-top:0.5rem; }
    ul li, ol li { margin-bottom:0.5rem; }
    table { width:100%; border-collapse:collapse; }
    th, td { border:1px solid #30363d; padding:0.75rem; text-align:left; }
    th { background:#30363d; }
  </style>
</head>
<body>
  <h1>🏛️ SmartLibrary</h1>
  <p class="subtitle">Soluție modernă pentru management inteligent al bibliotecii, construită cu Java 21 și design scalabil pentru orice dimensiune de colecție.</p>

  <details open>
    <summary>📌 Detalii proiect</summary>
    <p>📖 SmartLibrary revoluționează modul în care bibliotecile operează, oferind o soluție completă pentru catalogare inteligentă, împrumuturi rapide, gestionarea cozii de rezervări și organizarea de evenimente culturale, toate pe o bază modernă Java 21 și arhitectură scalabilă.</p>
    <ul>
      <li>📚 <strong>29 CLI</strong> – comenzi pentru gestionare completă a bibliotecii</li>
      <li>☕ <strong>Java 21</strong> – Records · switch‑expressions · Stream API</li>
      <li>🛡️ <strong>Audit Async</strong> – EventBus → AuditService (thread dedicat)</li>
      <li>🏫 <strong>User‑centric</strong> – cozi rezervare · penalizări automate · roluri STAFF/ADMIN</li>
    </ul>
  </details>

  <details>
    <summary>📋 Comenzi CLI</summary>
    <ol>
      <li>📚 Listare publicații</li>
      <li>🔍 Căutare după titlu</li>
      <li>✍️ Căutare după autor</li>
      <li>🗂️ Căutare după categorie</li>
      <li>⏳ Căutare după interval de ani</li>
      <li>🚦 Căutare după disponibilitate</li>
      <li>🧮 Căutare complexă + sortare multiplă</li>
      <li>🗓️ Sortare după anul publicării</li>
      <li>⭐ Sortare după rating</li>
      <li>🔢 Sortare după nr. de împrumuturi</li>
      <li>🔠 Sortare alfabetică</li>
      <li>📥 Împrumută publicație</li>
      <li>📤 Returnează publicație + penalizare</li>
      <li>📌 Rezervă publicație (FIFO)</li>
      <li>🔄 Reînnoiește împrumut</li>
      <li>📝 Adaugă recenzie</li>
      <li>👀 Vezi recenzii + rating mediu</li>
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
    <summary>🧩 Model de domeniu</summary>
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
        <tr><td>🔄 Împrumut</td><td>Date împrumut / returnare</td></tr>
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
    <summary>🔨 Pattern-uri</summary>
    <ul>
      <li>⚙️ Singleton – BibliotecaService, AuthService, EvenimentService, AuditService, EventBus</li>
      <li>🏭 Factory – CarteFactory, RevistaFactory, AudiobookFactory</li>
      <li>🔧 Builder – CititorBuilder, BibliotecarBuilder, EvenimentBuilder</li>
      <li>🎯 Strategy – SortContext + comparatoare runtime</li>
      <li>🔗 Chain of Responsibility – LimitaImprumuturiHandler → … → CoadaRezervariPlinaHandler</li>
      <li>👁️ Observer – EventBus → AuditService</li>
      <li>📦 DTO – PublicatieDTO</li>
    </ul>
  </details>
</body>
</html>
