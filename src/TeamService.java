import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamService {
    private Team team;
    private final List<Match> teamMatches = new ArrayList<>();
    private final Map<String, Integer> playerGoals = new HashMap<>();
    private final Map<String, Integer> playerAssists = new HashMap<>();

    public TeamService(Team team) {
        this.team = team;
    }

    /**
     * Adds a match to the team's match history.
     */
    public void addMatchToHistory(Match match) {
        if (!match.getHomeTeam().getId().equals(team.getId()) && 
            !match.getAwayTeam().getId().equals(team.getId())) {
            throw new IllegalArgumentException("This match is not related to team: " + team.getId());
        }
        teamMatches.add(match);
    }

    /**
     * Returns the number of matches played.
     */
    public int getMatchesPlayed() {
        return (int) teamMatches.stream()
                .filter(Match::isPlayed)
                .count();
    }

    /**
     * Returns the number of wins.
     */
    public int getWins() {
        return (int) teamMatches.stream()
                .filter(Match::isPlayed)
                .filter(match -> {
                    boolean isHome = match.getHomeTeam().getId().equals(team.getId());
                    int teamGoals = isHome ? match.getHomeGoals() : match.getAwayGoals();
                    int opponentGoals = isHome ? match.getAwayGoals() : match.getHomeGoals();
                    return teamGoals > opponentGoals;
                })
                .count();
    }

    /**
     * Returns the number of draws.
     */
    public int getDraws() {
        return (int) teamMatches.stream()
                .filter(Match::isPlayed)
                .filter(match -> match.getHomeGoals() == match.getAwayGoals())
                .count();
    }

    /**
     * Returns the number of losses.
     */
    public int getLosses() {
        return (int) teamMatches.stream()
                .filter(Match::isPlayed)
                .filter(match -> {
                    boolean isHome = match.getHomeTeam().getId().equals(team.getId());
                    int teamGoals = isHome ? match.getHomeGoals() : match.getAwayGoals();
                    int opponentGoals = isHome ? match.getAwayGoals() : match.getHomeGoals();
                    return teamGoals < opponentGoals;
                })
                .count();
    }

    /**
     * Returns total goals scored by the team.
     */
    public int getTotalGoalsFor() {
        return (int) teamMatches.stream()
                .filter(Match::isPlayed)
                .mapToInt(match -> {
                    boolean isHome = match.getHomeTeam().getId().equals(team.getId());
                    return isHome ? match.getHomeGoals() : match.getAwayGoals();
                })
                .sum();
    }

    /**
     * Returns total goals conceded by the team.
     */
    public int getTotalGoalsAgainst() {
        return (int) teamMatches.stream()
                .filter(Match::isPlayed)
                .mapToInt(match -> {
                    boolean isHome = match.getHomeTeam().getId().equals(team.getId());
                    return isHome ? match.getAwayGoals() : match.getHomeGoals();
                })
                .sum();
    }

    /**
     * Returns goal difference.
     */
    public int getGoalDifference() {
        return getTotalGoalsFor() - getTotalGoalsAgainst();
    }

    /**
     * Returns average goals per match.
     */
    public double getAverageGoalsPerMatch() {
        int played = getMatchesPlayed();
        if (played == 0) return 0.0;
        return (double) getTotalGoalsFor() / played;
    }

    /**
     * Returns the team's top goal scorer.
     */
    public Player getTopScorer() {
        return team.getRoster().stream()
                .max(Comparator.comparingInt(Player::getGoals))
                .orElse(null);
    }

    /**
     * Returns the top N players by goals.
     */
    public List<Player> getTopScorers(int limit) {
        return team.getRoster().stream()
                .sorted(Comparator.comparingInt(Player::getGoals).reversed())
                .limit(limit)
                .toList();
    }

    /**
     * Returns the player with the most assists.
     */
    public Player getTopAssister() {
        return team.getRoster().stream()
                .max(Comparator.comparingInt(Player::getAssists))
                .orElse(null);
    }

    /**
     * Returns the team statistics as a string.
     */
    public String getTeamStats() {
        return String.format(
                "%s Stats:\n" +
                "  Matches: %d | W: %d | D: %d | L: %d\n" +
                "  Goals: %d For - %d Against (Diff: %+d)\n" +
                "  Avg Goals/Match: %.2f",
                team.getName(),
                getMatchesPlayed(), getWins(), getDraws(), getLosses(),
                getTotalGoalsFor(), getTotalGoalsAgainst(), getGoalDifference(),
                getAverageGoalsPerMatch()
        );
    }

    /**
     * Returns the team's matches.
     */
    public List<Match> getTeamMatches() {
        return new ArrayList<>(teamMatches);
    }

    /**
     * Returns players sorted by goals.
     */
    public List<Player> getRosterSortedByGoals() {
        return team.getRoster().stream()
                .sorted(Comparator.comparingInt(Player::getGoals).reversed())
                .toList();
    }

    /**
     * Returns the managed team.
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Returns the head coach.
     */
    public Coach getHeadCoach() {
        return team.getHeadCoach();
    }

    /**
     * Returns the number of players on the roster.
     */
    public int getRosterSize() {
        return team.getRoster().size();
    }
}

