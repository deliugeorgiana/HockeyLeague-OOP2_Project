import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class LeagueService {
    private LeagueSeason season;
    private final List<Team> teams = new ArrayList<>();
    private final Map<String, Player> playersById = new HashMap<>();
    private final Set<Match> matches = new LinkedHashSet<>();
    private final List<Person> registeredPeople = new ArrayList<>();
    private final List<Contract> contracts = new ArrayList<>();
    private final Map<String, StandingEntry> standingsByTeamId = new HashMap<>();
    private final NavigableMap<Integer, List<StandingEntry>> standingsByPoints =
            new TreeMap<>(Comparator.reverseOrder());

    // 1
    public void createSeason(String seasonLabel, int startYear, int endYear) {
        this.season = new LeagueSeason(seasonLabel, startYear, endYear);
    }

    // 2
    public void registerTeam(Team team) {
        teams.add(team);
        standingsByTeamId.put(team.getId(), new StandingEntry(team));
    }

    // 3
    public void assignCoachToTeam(String teamId, Coach coach) {
        Team team = findTeamByIdOrThrow(teamId);
        team.setHeadCoach(coach);
        registeredPeople.add(coach);
    }

    // 4
    public void addPlayerToTeam(String teamId, Player player, double salary, int durationYears) {
        Team team = findTeamByIdOrThrow(teamId);
        team.addPlayer(player);
        playersById.put(player.getId(), player);
        registeredPeople.add(player);
        contracts.add(new Contract(player, team, salary, LocalDate.now(), durationYears));
    }

    // 5
    public boolean removePlayerFromTeam(String teamId, String playerId) {
        Team team = findTeamByIdOrThrow(teamId);
        boolean removed = team.removePlayerById(playerId);
        if (removed) {
            playersById.remove(playerId);
            contracts.removeIf(contract -> contract.getPlayer().getId().equals(playerId));
        }
        return removed;
    }

    // 6
    public void registerReferee(Referee referee) {
        registeredPeople.add(referee);
    }

    // 7
    public void scheduleMatch(Match match) {
        matches.add(match);
    }

    // 8
    public void recordMatchResult(String matchId, int homeGoals, int awayGoals) {
        Match match = findMatchByIdOrThrow(matchId);
        if (match.isPlayed()) {
            throw new IllegalStateException("Match already has a result: " + matchId);
        }

        match.setResult(homeGoals, awayGoals);

        StandingEntry homeEntry = standingsByTeamId.get(match.getHomeTeam().getId());
        StandingEntry awayEntry = standingsByTeamId.get(match.getAwayTeam().getId());
        homeEntry.registerGame(homeGoals, awayGoals);
        awayEntry.registerGame(awayGoals, homeGoals);

        rebuildSortedStandings();
    }

    // 9
    public Optional<Player> findPlayerById(String playerId) {
        return Optional.ofNullable(playersById.get(playerId));
    }

    // 10
    public List<Player> listTeamRoster(String teamId) {
        Team team = findTeamByIdOrThrow(teamId);
        return team.getRoster();
    }

    // 11
    public List<Match> listMatches() {
        return new ArrayList<>(matches);
    }

    // 12
    public List<StandingEntry> getStandings() {
        List<StandingEntry> result = new ArrayList<>();
        for (List<StandingEntry> samePoints : standingsByPoints.values()) {
            samePoints.sort(Comparator.comparingInt(StandingEntry::getGoalDifference).reversed());
            result.addAll(samePoints);
        }
        return result;
    }

    public List<Player> getTopScorers(int limit) {
        return playersById.values().stream()
                .sorted(Comparator.comparingInt(Player::getGoals).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public LeagueSeason getSeason() {
        return season;
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams);
    }

    public List<Contract> getContracts() {
        return new ArrayList<>(contracts);
    }

    public List<Person> getRegisteredPeople() {
        return new ArrayList<>(registeredPeople);
    }

    public void transferPlayer(String playerId, String fromTeamId, String toTeamId, double newSalary, int durationYears) {
        Team from = findTeamByIdOrThrow(fromTeamId);
        Team to = findTeamByIdOrThrow(toTeamId);
        Player player = playersById.get(playerId);

        if (player == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }

        boolean removed = from.removePlayerById(playerId);
        if (!removed) {
            throw new IllegalStateException("Player is not in source team: " + fromTeamId);
        }

        to.addPlayer(player);
        contracts.removeIf(contract -> contract.getPlayer().getId().equals(playerId));
        contracts.add(new Contract(player, to, newSalary, LocalDate.now(), durationYears));
    }

    private Team findTeamByIdOrThrow(String teamId) {
        return teams.stream()
                .filter(team -> team.getId().equals(teamId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));
    }

    private Match findMatchByIdOrThrow(String matchId) {
        return matches.stream()
                .filter(match -> match.getId().equals(matchId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));
    }

    private void rebuildSortedStandings() {
        standingsByPoints.clear();
        for (StandingEntry entry : standingsByTeamId.values()) {
            standingsByPoints.computeIfAbsent(entry.getPoints(), ignored -> new ArrayList<>()).add(entry);
        }
    }
}

