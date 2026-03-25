import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LeagueService service = new LeagueService();

        service.createSeason("Liga Nationala de Hochei", 2026, 2027);

        Team wolves = new Team("T1", "Ice Wolves", "Bucuresti");
        Team bears = new Team("T2", "North Bears", "Cluj");
        service.registerTeam(wolves);
        service.registerTeam(bears);

        Coach coachWolves = new Coach("C1", "Andrei", "Pop", 45, 18, "Offensive");
        Coach coachBears = new Coach("C2", "Mihai", "Ionescu", 50, 22, "Defensive");
        service.assignCoachToTeam("T1", coachWolves);
        service.assignCoachToTeam("T2", coachBears);

        Player p1 = new Player("P1", "Alex", "Marin", 24, "Center", 9);
        Player p2 = new Player("P2", "Vlad", "Georgescu", 22, "Wing", 17);
        Player p3 = new Player("P3", "Radu", "Stan", 26, "Defense", 5);
        Player p4 = new Player("P4", "Ionut", "Dobre", 23, "Center", 11);

        service.addPlayerToTeam("T1", p1, 120000, 2);
        service.addPlayerToTeam("T1", p2, 90000, 1);
        service.addPlayerToTeam("T2", p3, 100000, 2);
        service.addPlayerToTeam("T2", p4, 95000, 2);

        Referee referee = new Referee("R1", "Sorin", "Matei", 39, "Elite");
        service.registerReferee(referee);

        Arena arenaBuc = new Arena("Patinoar Bucuresti", "Bucuresti", 5000);
        Arena arenaCluj = new Arena("Patinoar Cluj", "Cluj", 4500);

        Match m1 = new Match("M1", wolves, bears, arenaBuc, LocalDate.of(2026, 10, 10));
        Match m2 = new Match("M2", bears, wolves, arenaCluj, LocalDate.of(2026, 11, 3));
        service.scheduleMatch(m1);
        service.scheduleMatch(m2);

        service.recordMatchResult("M1", 4, 2);
        p1.registerGoal();
        p1.registerAssist();
        p2.registerGoal();

        service.recordMatchResult("M2", 1, 3);
        p1.registerGoal();
        p4.registerGoal();

        service.transferPlayer("P2", "T1", "T2", 110000, 3);

        System.out.println("=== Season ===");
        System.out.println(service.getSeason());

        System.out.println("\n=== Teams ===");
        for (Team team : service.getTeams()) {
            System.out.println(team + " | Coach: " + team.getHeadCoach());
        }

        System.out.println("\n=== Roster T2 ===");
        for (Player player : service.listTeamRoster("T2")) {
            System.out.println(player);
        }

        System.out.println("\n=== Matches ===");
        for (Match match : service.listMatches()) {
            System.out.println(match);
        }

        System.out.println("\n=== Standings (sorted by points) ===");
        for (StandingEntry entry : service.getStandings()) {
            System.out.println(entry);
        }

        System.out.println("\n=== Search player P1 ===");
        System.out.println(service.findPlayerById("P1").orElse(null));

        System.out.println("\n=== Top scorers ===");
        for (Player player : service.getTopScorers(3)) {
            System.out.println(player);
        }

        System.out.println("\n=== Contracts ===");
        for (Contract contract : service.getContracts()) {
            System.out.println(contract);
        }

        System.out.println("\n=== Registered people (inheritance in one collection) ===");
        for (Person person : service.getRegisteredPeople()) {
            System.out.println(person.getRole() + ": " + person.getFullName());
        }
    }
}
