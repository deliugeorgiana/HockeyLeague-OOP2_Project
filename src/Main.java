import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static LeagueService service;
    private static TeamDAO teamDAO;
    private static PlayerDAO playerDAO;
    private static CoachDAO coachDAO;
    private static RefereeDAO refereeDAO;
    private static ArenaDAO arenaDAO;
    private static MatchDAO matchDAO;
    private static ContractDAO contractDAO;
    private static StandingDAO standingDAO;
    private static List<Team> teams = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    private static List<Coach> coaches = new ArrayList<>();
    private static List<Referee> referees = new ArrayList<>();
    private static List<Arena> arenas = new ArrayList<>();
    private static int matchCounter = 0;
    private static int teamCounter = 0;
    private static int playerCounter = 0;
    private static int coachCounter = 0;
    private static int refereeCounter = 0;

    public static void main(String[] args) {
        // Initialize database
        System.out.println("=== Initializing Database ===");
        DatabaseInitializer.initializeDatabase();
        
        System.out.println("\n=== Creating DAOs ===");
        teamDAO = new TeamDAO();
        playerDAO = new PlayerDAO();
        coachDAO = new CoachDAO();
        refereeDAO = new RefereeDAO();
        arenaDAO = new ArenaDAO();
        matchDAO = new MatchDAO();
        contractDAO = new ContractDAO();
        standingDAO = new StandingDAO();
        
        service = new LeagueService();

        // Load existing data from database so the app doesn't work only in-memory
        System.out.println("\n=== Loading existing data from database ===");
        loadDataFromDatabase();

        displayMenu();
    }

    private static void loadDataFromDatabase() {
        // Teams
        teams = teamDAO.findAll();
        for (Team t : teams) {
            service.registerTeam(t);
        }
        teamCounter = teams.size();

        // Arenas
        arenas = arenaDAO.findAll();

        // Players
        players = playerDAO.findAll();
        playerCounter = players.size();

        System.out.println("Loaded teams: " + teams.size());
        System.out.println("Loaded arenas: " + arenas.size());
        System.out.println("Loaded players: " + players.size());
    }

    private static void displayMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("HOCKEY LEAGUE MANAGEMENT SYSTEM");
            System.out.println("=".repeat(50));
            System.out.println("1. Create Season");
            System.out.println("2. Register Team");
            System.out.println("3. Assign Coach to Team");
            System.out.println("4. Add Player to Team");
            System.out.println("5. Register Referee");
            System.out.println("6. Register Arena");
            System.out.println("7. Schedule Match");
            System.out.println("8. Record Match Result");
            System.out.println("9. View Standings");
            System.out.println("10. View Top Scorers");
            System.out.println("11. View All Teams");
            System.out.println("12. View Team Roster");
            System.out.println("13. View All Matches");
            System.out.println("14. View Contracts");
            System.out.println("15. Transfer Player");
            System.out.println("16. View Database Contents");
            System.out.println("0. Exit");
            System.out.println("=".repeat(50));
            System.out.print("Choose an option: ");
            
            int choice = readInt();
            
            switch (choice) {
                case 1:
                    createSeason();
                    break;
                case 2:
                    registerTeam();
                    break;
                case 3:
                    assignCoachToTeam();
                    break;
                case 4:
                    addPlayerToTeam();
                    break;
                case 5:
                    registerReferee();
                    break;
                case 6:
                    registerArena();
                    break;
                case 7:
                    scheduleMatch();
                    break;
                case 8:
                    recordMatchResult();
                    break;
                case 9:
                    viewStandings();
                    break;
                case 10:
                    viewTopScorers();
                    break;
                case 11:
                    viewAllTeams();
                    break;
                case 12:
                    viewTeamRoster();
                    break;
                case 13:
                    viewAllMatches();
                    break;
                case 14:
                    viewContracts();
                    break;
                case 15:
                    transferPlayer();
                    break;
                case 16:
                    viewDatabaseContents();
                    break;
                case 0:
                    running = false;
                    System.out.println("\nClosing database connection...");
                    DatabaseConnection.getInstance().closeConnection();
                    System.out.println("Application finished successfully.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void createSeason() {
        System.out.print("\nEnter season label (e.g., 'National League 2026'): ");
        String label = scanner.nextLine();
        System.out.print("Enter start year: ");
        int startYear = readInt();
        System.out.print("Enter end year: ");
        int endYear = readInt();
        
        service.createSeason(label, startYear, endYear);
        System.out.println("Season created: " + service.getSeason());
    }

    private static void registerTeam() {
        System.out.print("\nEnter team name: ");
        String name = scanner.nextLine();
        System.out.print("Enter team city: ");
        String city = scanner.nextLine();
        
        String teamId = "T" + (++teamCounter);
        Team team = new Team(teamId, name, city);
        teams.add(team);
        service.registerTeam(team);
        teamDAO.create(team);
        
        StandingEntry standing = new StandingEntry(team);
        standingDAO.create(standing);
        
        System.out.println("Team registered: " + team);
    }

    private static void assignCoachToTeam() {
        if (teams.isEmpty()) {
            System.out.println("No teams registered.");
            return;
        }
        
        System.out.println("\nAvailable teams:");
        for (int i = 0; i < teams.size(); i++) {
            System.out.println((i + 1) + ". " + teams.get(i));
        }
        System.out.print("Select team: ");
        int teamIdx = readInt() - 1;
        
        if (teamIdx < 0 || teamIdx >= teams.size()) {
            System.out.println("Invalid team.");
            return;
        }
        
        Team team = teams.get(teamIdx);
        System.out.print("Enter coach first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter coach last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter coach age: ");
        int age = readInt();
        System.out.print("Enter experience years: ");
        int experience = readInt();
        System.out.print("Enter strategy style (Offensive/Defensive/Balanced): ");
        String strategy = scanner.nextLine();
        
        String coachId = "C" + (++coachCounter);
        Coach coach = new Coach(coachId, firstName, lastName, age, experience, strategy);
        coaches.add(coach);
        service.assignCoachToTeam(team.getId(), coach);
        coachDAO.create(coach);
        coachDAO.assignToTeam(coachId, team.getId());
        
        System.out.println("Coach assigned: " + coach + " to " + team.getName());
    }

    private static void addPlayerToTeam() {
        if (teams.isEmpty()) {
            System.out.println("No teams registered.");
            return;
        }
        
        System.out.println("\nAvailable teams:");
        for (int i = 0; i < teams.size(); i++) {
            System.out.println((i + 1) + ". " + teams.get(i));
        }
        System.out.print("Select team: ");
        int teamIdx = readInt() - 1;
        
        if (teamIdx < 0 || teamIdx >= teams.size()) {
            System.out.println("Invalid team.");
            return;
        }
        
        Team team = teams.get(teamIdx);
        System.out.print("Enter player first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter player last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter player age: ");
        int age = readInt();
        System.out.print("Enter player position (Center/Wing/Defense): ");
        String position = scanner.nextLine();
        System.out.print("Enter jersey number: ");
        int jersey = readInt();
        System.out.print("Enter player salary: ");
        double salary = readDouble();
        System.out.print("Enter contract duration (years): ");
        int duration = readInt();
        
        String playerId = "P" + (++playerCounter);
        Player player = new Player(playerId, firstName, lastName, age, position, jersey);
        players.add(player);
        service.addPlayerToTeam(team.getId(), player, salary, duration);
        playerDAO.create(player);
        playerDAO.assignToTeam(playerId, team.getId());
        contractDAO.create(new Contract(player, team, salary, LocalDate.now(), duration));
        
        System.out.println("Player added: " + player + " to " + team.getName());
    }

    private static void registerReferee() {
        System.out.print("\nEnter referee first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter referee last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter referee age: ");
        int age = readInt();
        System.out.print("Enter referee level (Elite/Senior/Junior): ");
        String level = scanner.nextLine();
        
        String refereeId = "R" + (++refereeCounter);
        Referee referee = new Referee(refereeId, firstName, lastName, age, level);
        referees.add(referee);
        service.registerReferee(referee);
        refereeDAO.create(referee);
        
        System.out.println("Referee registered: " + referee);
    }

    private static void registerArena() {
        System.out.print("\nEnter arena name: ");
        String name = scanner.nextLine();
        System.out.print("Enter arena city: ");
        String city = scanner.nextLine();
        System.out.print("Enter arena capacity: ");
        int capacity = readInt();
        
        Arena arena = new Arena(name, city, capacity);
        arenas.add(arena);
        arenaDAO.create(arena);
        
        System.out.println("Arena registered: " + arena);
    }

    private static void scheduleMatch() {
        if (teams.size() < 2) {
            System.out.println("At least 2 teams are required.");
            return;
        }
        
        if (arenas.isEmpty()) {
            System.out.println("No arenas registered.");
            return;
        }
        
        System.out.println("\nAvailable teams:");
        for (int i = 0; i < teams.size(); i++) {
            System.out.println((i + 1) + ". " + teams.get(i));
        }
        System.out.print("Select home team: ");
        Team homeTeam = teams.get(readInt() - 1);
        
        System.out.print("Select away team: ");
        Team awayTeam = teams.get(readInt() - 1);
        
        System.out.println("\nAvailable arenas:");
        for (int i = 0; i < arenas.size(); i++) {
            System.out.println((i + 1) + ". " + arenas.get(i));
        }
        System.out.print("Select arena: ");
        Arena arena = arenas.get(readInt() - 1);
        
        System.out.print("Enter match date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);
        
        String matchId = "M" + (++matchCounter);
        Match match = new Match(matchId, homeTeam, awayTeam, arena, date);
        service.scheduleMatch(match);
        int arenaId = arenaDAO.findIdByName(arena.getName()).orElse(1);
        matchDAO.create(match, arenaId);
        
        System.out.println("Match scheduled: " + match);
    }

    private static void recordMatchResult() {
        List<Match> matches = service.listMatches();
        if (matches.isEmpty()) {
            System.out.println("No matches scheduled.");
            return;
        }
        
        System.out.println("\nAvailable matches:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + ". " + matches.get(i));
        }
        System.out.print("Select match: ");
        Match match = matches.get(readInt() - 1);
        
        System.out.print("Enter home team goals: ");
        int homeGoals = readInt();
        System.out.print("Enter away team goals: ");
        int awayGoals = readInt();
        
        service.recordMatchResult(match.getId(), homeGoals, awayGoals);
        matchDAO.updateResult(match.getId(), homeGoals, awayGoals);
        
        System.out.println("Match result recorded.");
    }

    private static void viewStandings() {
        List<StandingEntry> standings = service.getStandings();
        if (standings.isEmpty()) {
            System.out.println("\nNo standings available.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STANDINGS (Sorted by Points)");
        System.out.println("=".repeat(60));
        for (StandingEntry entry : standings) {
            System.out.println(entry);
        }
    }

    private static void viewTopScorers() {
        System.out.print("\nHow many top scorers to display? ");
        int limit = readInt();
        List<Player> topScorers = service.getTopScorers(limit);
        
        if (topScorers.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TOP " + limit + " SCORERS");
        System.out.println("=".repeat(60));
        for (Player player : topScorers) {
            System.out.println(player.getFullName() + " - Goals: " + player.getGoals());
        }
    }

    private static void viewAllTeams() {
        List<Team> allTeams = service.getTeams();
        if (allTeams.isEmpty()) {
            System.out.println("\nNo teams registered.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL TEAMS");
        System.out.println("=".repeat(60));
        for (Team team : allTeams) {
            System.out.println(team + " | Coach: " + team.getHeadCoach());
        }
    }

    private static void viewTeamRoster() {
        if (teams.isEmpty()) {
            System.out.println("No teams registered.");
            return;
        }
        
        System.out.println("\nAvailable teams:");
        for (int i = 0; i < teams.size(); i++) {
            System.out.println((i + 1) + ". " + teams.get(i));
        }
        System.out.print("Select team: ");
        Team team = teams.get(readInt() - 1);
        
        List<Player> roster = service.listTeamRoster(team.getId());
        if (roster.isEmpty()) {
            System.out.println("\nTeam has no players.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ROSTER: " + team.getName());
        System.out.println("=".repeat(60));
        for (Player player : roster) {
            System.out.println(player);
        }
    }

    private static void viewAllMatches() {
        List<Match> matches = service.listMatches();
        if (matches.isEmpty()) {
            System.out.println("\nNo matches scheduled.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL MATCHES");
        System.out.println("=".repeat(60));
        for (Match match : matches) {
            System.out.println(match);
        }
    }

    private static void viewContracts() {
        List<Contract> contracts = service.getContracts();
        if (contracts.isEmpty()) {
            System.out.println("\nNo contracts found.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL CONTRACTS");
        System.out.println("=".repeat(60));
        for (Contract contract : contracts) {
            System.out.println(contract);
        }
    }

    private static void transferPlayer() {
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        
        System.out.println("\nAvailable players:");
        for (int i = 0; i < players.size(); i++) {
            System.out.println((i + 1) + ". " + players.get(i));
        }
        System.out.print("Select player to transfer: ");
        Player player = players.get(readInt() - 1);
        
        System.out.println("\nAvailable teams:");
        for (int i = 0; i < teams.size(); i++) {
            System.out.println((i + 1) + ". " + teams.get(i));
        }
        System.out.print("Select new team: ");
        Team newTeam = teams.get(readInt() - 1);
        
        System.out.print("Enter new salary: ");
        double newSalary = readDouble();
        System.out.print("Enter contract duration (years): ");
        int duration = readInt();
        
        service.transferPlayer(player.getId(), player.getCurrentTeamId(), newTeam.getId(), newSalary, duration);
        contractDAO.create(new Contract(player, newTeam, newSalary, LocalDate.now(), duration));
        
        System.out.println("Player transferred.");
    }

    private static void viewDatabaseContents() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DATABASE CONTENTS");
        System.out.println("=".repeat(60));
        
        System.out.println("\n=== TEAMS FROM DATABASE ===");
        for (Team team : teamDAO.findAll()) {
            System.out.println(team);
        }
        
        System.out.println("\n=== PLAYERS FROM DATABASE ===");
        for (Player player : playerDAO.findAll()) {
            System.out.println(player);
        }
        
        System.out.println("\n=== MATCHES FROM DATABASE ===");
        for (Match match : matchDAO.findPlayedMatches()) {
            System.out.println(match);
        }
        
        System.out.println("\n=== STANDINGS FROM DATABASE ===");
        for (StandingEntry entry : standingDAO.findAllSorted()) {
            System.out.println(entry);
        }
        
        System.out.println("\n=== CONTRACTS FROM DATABASE ===");
        for (Contract contract : contractDAO.findAll()) {
            System.out.println(contract);
        }
        
        System.out.println("\n=== TEAM SALARY TOTALS ===");
        for (Team team : teams) {
            double totalSalary = contractDAO.getTotalTeamSalary(team.getId());
            System.out.println(team.getName() + " salary budget: $" + totalSalary);
        }
    }


    private static int readInt() {
        int value = 0;
        try {
            value = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
        return value;
    }

    private static double readDouble() {
        double value = 0.0;
        try {
            value = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
        return value;
    }
}
