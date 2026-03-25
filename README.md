# Sistem de gestiune a unei ligi de hochei - Etapa I

Acest proiect implementeaza cerintele Etapa I in Java, fara dependinte externe.

## 1) Definirea sistemului

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

## 2) Implementare

Cerintele sunt acoperite astfel:
- Clase cu atribute `private` / `protected` + metode de acces in toate entitatile.
- Cel putin 2 colectii diferite:
  - `List` / `Set` pentru echipe, persoane si meciuri.
  - `TreeMap` (sortata) pentru clasament dupa puncte.
- Mostenire: `Player`, `Coach`, `Referee` mostenesc `Person`.
- Clasa serviciu: `LeagueService` expune operatiile sistemului.
- `Main` ruleaza un scenariu demo complet.

## Rulare

```bash
javac src/*.java
java -cp src Main
```

