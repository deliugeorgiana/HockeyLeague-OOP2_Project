# Sistem de gestiune a unei ligi de hochei (Java) — Etapa I + Etapa II

Aplicatie Java pentru gestionarea unei ligi de hochei.

- **Etapa I**: modelare OOP + operatii in memorie (servicii + colectii).
- **Etapa II**: persistenta in **PostgreSQL** prin strat **DAO (CRUD + interogari)**. Datele se pastreaza intre rulari.

## Cerinte acoperite

### Actiuni / interogari disponibile (minim 10)
1. Creare sezon (`createSeason`)
2. Inregistrare echipa (`registerTeam`)
3. Atribuire antrenor unei echipe (`assignCoachToTeam`)
4. Adaugare jucator in echipa + contract (`addPlayerToTeam`)
5. Eliminare jucator din echipa (`removePlayerFromTeam`)
6. Inregistrare arbitru (`registerReferee`)
7. Programare meci (`scheduleMatch`)
8. Inregistrare rezultat meci (`recordMatchResult`)
9. Transfer jucator intre echipe (`transferPlayer`)
10. Cautare jucator dupa id (`findPlayerById`)
11. Listare lot echipa (`listTeamRoster`)
12. Listare meciuri (`listMatches`)
13. Clasament curent sortat dupa puncte (`getStandings`)
14. Top marcatori (`getTopScorers`)

### Tipuri de obiecte (minim 8)
1. `Person` (clasa de baza)
2. `Player`
3. `Coach`
4. `Referee`
5. `Team`
6. `Arena`
7. `Match`
8. `LeagueSeason`
9. `StandingEntry`
10. `Contract`
11. `LeagueService` (clasa serviciu)

## Arhitectura si implementare

### Domeniu (OOP)
- Clase cu atribute `private` / `protected` + metode de acces in entitati.
- Mostenire: `Player`, `Coach`, `Referee` mostenesc `Person`.
- Colectii folosite (minim 2 tipuri):
  - `List` / `Set` pentru echipe, persoane si meciuri.
  - `TreeMap` pentru clasament sortat dupa puncte.
- Service layer: `LeagueService`, `TeamService`.
- `Main` ruleaza un scenariu demo.

### Persistenta (Etapa II)
- DAO layer (persistenta): `ArenaDAO`, `TeamDAO`, `PlayerDAO`, `CoachDAO`, `RefereeDAO`, `MatchDAO`, `ContractDAO`, `StandingDAO`.
- Conexiune DB: `DatabaseConnection`.
- Initializare DB (schema + date default, daca este necesar): `DatabaseInitializer`.

## Baza de date (PostgreSQL)
- Nume baza de date: `hockey_league`
- Script schema + seed: `create_database.sql`

Scriptul creeaza tabelele principale (`arena`, `team`, `person`, `player`, `coach`, `referee`, `contract`, `match`, `standing_entry`, etc.) si indexuri pentru performanta.

### Setup (pgAdmin / psql)
1. Creeaza baza de date `hockey_league`.
2. Ruleaza scriptul `create_database.sql` conectat pe baza `hockey_league`.
3. Verifica setarile de conectare din `DatabaseConnection` (host/port/user/parola).

> Recomandare: nu hardcoda parole in README. Ajusteaza local credintele folosite in `DatabaseConnection`.

### Persistenta datelor
Operatiile din service-uri folosesc DAO-urile pentru a scrie/citi din PostgreSQL. Astfel, entitatile introduse pe parcurs (echipe, jucatori, meciuri, contracte, clasament) raman salvate in baza de date intre rulari.

## Rulare

### Varianta recomandata: IntelliJ IDEA
1. Deschide proiectul in IntelliJ.
2. Adauga in proiect driverul **PostgreSQL JDBC** (library `postgresql-*.jar`).
3. Ruleaza `Main`.

### Linie de comanda (orientativ)
Ai nevoie de driverul JDBC PostgreSQL in classpath.

```bash
javac -cp "src;path\\to\\postgresql-jdbc.jar" src\\*.java
java -cp "src;path\\to\\postgresql-jdbc.jar" Main
```

