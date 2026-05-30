import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for StandingEntry entity
 */
public class StandingDAO {
    private final Connection connection;

    public StandingDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Create a new standing entry in database
     */
    public void create(StandingEntry entry) {
        String sql = "INSERT INTO standing_entry (team_id, matches_played, wins, draws, losses, goals_for, goals_against, points) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entry.getTeam().getId());
            stmt.setInt(2, entry.getMatchesPlayed());
            stmt.setInt(3, entry.getWins());
            stmt.setInt(4, entry.getDraws());
            stmt.setInt(5, entry.getLosses());
            stmt.setInt(6, entry.getGoalsFor());
            stmt.setInt(7, entry.getGoalsAgainst());
            stmt.setInt(8, entry.getPoints());
            stmt.executeUpdate();
            System.out.println("Standing entry created for: " + entry.getTeam().getName());
        } catch (SQLException e) {
            System.err.println("Error creating standing entry: " + e.getMessage());
        }
    }

    /**
     * Find standing entry by team ID
     */
    public Optional<StandingEntry> findByTeamId(String teamId) {
        String sql = "SELECT se.team_id, se.matches_played, se.wins, se.draws, se.losses, se.goals_for, se.goals_against, se.points " +
                "FROM standing_entry se WHERE se.team_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TeamDAO teamDAO = new TeamDAO();
                Team team = teamDAO.findById(rs.getString("team_id")).orElse(null);

                if (team != null) {
                    StandingEntry entry = new StandingEntry(team);
                    entry.setMatchesPlayed(rs.getInt("matches_played"));
                    entry.setWins(rs.getInt("wins"));
                    entry.setDraws(rs.getInt("draws"));
                    entry.setLosses(rs.getInt("losses"));
                    entry.setGoalsFor(rs.getInt("goals_for"));
                    entry.setGoalsAgainst(rs.getInt("goals_against"));
                    return Optional.of(entry);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding standing entry: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Get all standings sorted by points
     */
    public List<StandingEntry> findAllSorted() {
        List<StandingEntry> standings = new ArrayList<>();
        String sql = "SELECT se.team_id, se.matches_played, se.wins, se.draws, se.losses, se.goals_for, se.goals_against, se.points " +
                "FROM standing_entry se ORDER BY se.points DESC, (se.goals_for - se.goals_against) DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            TeamDAO teamDAO = new TeamDAO();

            while (rs.next()) {
                Team team = teamDAO.findById(rs.getString("team_id")).orElse(null);

                if (team != null) {
                    StandingEntry entry = new StandingEntry(team);
                    entry.setMatchesPlayed(rs.getInt("matches_played"));
                    entry.setWins(rs.getInt("wins"));
                    entry.setDraws(rs.getInt("draws"));
                    entry.setLosses(rs.getInt("losses"));
                    entry.setGoalsFor(rs.getInt("goals_for"));
                    entry.setGoalsAgainst(rs.getInt("goals_against"));
                    standings.add(entry);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving standings: " + e.getMessage());
        }
        return standings;
    }

    /**
     * Update standing entry
     */
    public void update(StandingEntry entry) {
        String sql = "UPDATE standing_entry SET matches_played = ?, wins = ?, draws = ?, losses = ?, goals_for = ?, goals_against = ?, points = ? " +
                "WHERE team_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, entry.getMatchesPlayed());
            stmt.setInt(2, entry.getWins());
            stmt.setInt(3, entry.getDraws());
            stmt.setInt(4, entry.getLosses());
            stmt.setInt(5, entry.getGoalsFor());
            stmt.setInt(6, entry.getGoalsAgainst());
            stmt.setInt(7, entry.getPoints());
            stmt.setString(8, entry.getTeam().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating standing entry: " + e.getMessage());
        }
    }

    /**
     * Delete standing entry by team ID
     */
    public void deleteByTeamId(String teamId) {
        String sql = "DELETE FROM standing_entry WHERE team_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            stmt.executeUpdate();
            System.out.println("Standing entry deleted for team: " + teamId);
        } catch (SQLException e) {
            System.err.println("Error deleting standing entry: " + e.getMessage());
        }
    }

    /**
     * Get top N teams by points
     */
    public List<StandingEntry> findTopTeams(int limit) {
        List<StandingEntry> standings = new ArrayList<>();
        String sql = "SELECT se.team_id, se.matches_played, se.wins, se.draws, se.losses, se.goals_for, se.goals_against, se.points " +
                "FROM standing_entry se ORDER BY se.points DESC LIMIT ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            TeamDAO teamDAO = new TeamDAO();

            while (rs.next()) {
                Team team = teamDAO.findById(rs.getString("team_id")).orElse(null);

                if (team != null) {
                    StandingEntry entry = new StandingEntry(team);
                    entry.setMatchesPlayed(rs.getInt("matches_played"));
                    entry.setWins(rs.getInt("wins"));
                    entry.setDraws(rs.getInt("draws"));
                    entry.setLosses(rs.getInt("losses"));
                    entry.setGoalsFor(rs.getInt("goals_for"));
                    entry.setGoalsAgainst(rs.getInt("goals_against"));
                    standings.add(entry);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving top teams: " + e.getMessage());
        }
        return standings;
    }
}

