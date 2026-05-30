import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Match entity
 */
public class MatchDAO {
    private final Connection connection;

    public MatchDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Create a new match in database
     */
    public void create(Match match, int arenaId) {
        String sql = "INSERT INTO match (id, home_team_id, away_team_id, arena_id, match_date, is_played, home_goals, away_goals) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, match.getId());
            stmt.setString(2, match.getHomeTeam().getId());
            stmt.setString(3, match.getAwayTeam().getId());
            stmt.setInt(4, arenaId);
            stmt.setDate(5, java.sql.Date.valueOf(match.getDate()));
            stmt.setBoolean(6, match.isPlayed());
            stmt.setInt(7, match.getHomeGoals());
            stmt.setInt(8, match.getAwayGoals());
            stmt.executeUpdate();
            System.out.println("Match created: " + match.getId());
        } catch (SQLException e) {
            System.err.println("Error creating match: " + e.getMessage());
        }
    }

    /**
     * Find match by ID
     */
    public Optional<Match> findById(String id) {
        String sql = "SELECT m.id, m.home_team_id, m.away_team_id, m.arena_id, m.match_date, m.is_played, m.home_goals, m.away_goals " +
                "FROM match m WHERE m.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TeamDAO teamDAO = new TeamDAO();
                Team homeTeam = teamDAO.findById(rs.getString("home_team_id")).orElse(null);
                Team awayTeam = teamDAO.findById(rs.getString("away_team_id")).orElse(null);

                ArenaDAO arenaDAO = new ArenaDAO();
                Arena arena = arenaDAO.findById(rs.getInt("arena_id")).orElse(null);

                Match match = new Match(
                        rs.getString("id"),
                        homeTeam,
                        awayTeam,
                        arena,
                        rs.getDate("match_date").toLocalDate()
                );

                if (rs.getBoolean("is_played")) {
                    match.setResult(rs.getInt("home_goals"), rs.getInt("away_goals"));
                }

                return Optional.of(match);
            }
        } catch (SQLException e) {
            System.err.println("Error finding match: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Get all matches
     */
    public List<Match> findAll() {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT m.id, m.home_team_id, m.away_team_id, m.arena_id, m.match_date, m.is_played, m.home_goals, m.away_goals " +
                "FROM match m ORDER BY m.match_date";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            TeamDAO teamDAO = new TeamDAO();
            ArenaDAO arenaDAO = new ArenaDAO();

            while (rs.next()) {
                Team homeTeam = teamDAO.findById(rs.getString("home_team_id")).orElse(null);
                Team awayTeam = teamDAO.findById(rs.getString("away_team_id")).orElse(null);
                Arena arena = arenaDAO.findById(rs.getInt("arena_id")).orElse(null);

                Match match = new Match(
                        rs.getString("id"),
                        homeTeam,
                        awayTeam,
                        arena,
                        rs.getDate("match_date").toLocalDate()
                );

                if (rs.getBoolean("is_played")) {
                    match.setResult(rs.getInt("home_goals"), rs.getInt("away_goals"));
                }

                matches.add(match);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving matches: " + e.getMessage());
        }
        return matches;
    }

    /**
     * Update match result
     */
    public void updateResult(String matchId, int homeGoals, int awayGoals) {
        String sql = "UPDATE match SET is_played = true, home_goals = ?, away_goals = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, homeGoals);
            stmt.setInt(2, awayGoals);
            stmt.setString(3, matchId);
            stmt.executeUpdate();
            System.out.println("Match result updated: " + matchId);
        } catch (SQLException e) {
            System.err.println("Error updating match result: " + e.getMessage());
        }
    }

    /**
     * Delete match by ID
     */
    public void delete(String id) {
        String sql = "DELETE FROM match WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            System.out.println("Match deleted: " + id);
        } catch (SQLException e) {
            System.err.println("Error deleting match: " + e.getMessage());
        }
    }

    /**
     * Get matches by team ID
     */
    public List<Match> findByTeamId(String teamId) {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT m.id, m.home_team_id, m.away_team_id, m.arena_id, m.match_date, m.is_played, m.home_goals, m.away_goals " +
                "FROM match m WHERE m.home_team_id = ? OR m.away_team_id = ? ORDER BY m.match_date";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            stmt.setString(2, teamId);
            ResultSet rs = stmt.executeQuery();

            TeamDAO teamDAO = new TeamDAO();
            ArenaDAO arenaDAO = new ArenaDAO();

            while (rs.next()) {
                Team homeTeam = teamDAO.findById(rs.getString("home_team_id")).orElse(null);
                Team awayTeam = teamDAO.findById(rs.getString("away_team_id")).orElse(null);
                Arena arena = arenaDAO.findById(rs.getInt("arena_id")).orElse(null);

                Match match = new Match(
                        rs.getString("id"),
                        homeTeam,
                        awayTeam,
                        arena,
                        rs.getDate("match_date").toLocalDate()
                );

                if (rs.getBoolean("is_played")) {
                    match.setResult(rs.getInt("home_goals"), rs.getInt("away_goals"));
                }

                matches.add(match);
            }
        } catch (SQLException e) {
            System.err.println("Error finding matches by team: " + e.getMessage());
        }
        return matches;
    }

    /**
     * Get played matches only
     */
    public List<Match> findPlayedMatches() {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT m.id, m.home_team_id, m.away_team_id, m.arena_id, m.match_date, m.is_played, m.home_goals, m.away_goals " +
                "FROM match m WHERE m.is_played = true ORDER BY m.match_date DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            TeamDAO teamDAO = new TeamDAO();
            ArenaDAO arenaDAO = new ArenaDAO();

            while (rs.next()) {
                Team homeTeam = teamDAO.findById(rs.getString("home_team_id")).orElse(null);
                Team awayTeam = teamDAO.findById(rs.getString("away_team_id")).orElse(null);
                Arena arena = arenaDAO.findById(rs.getInt("arena_id")).orElse(null);

                Match match = new Match(
                        rs.getString("id"),
                        homeTeam,
                        awayTeam,
                        arena,
                        rs.getDate("match_date").toLocalDate()
                );

                match.setResult(rs.getInt("home_goals"), rs.getInt("away_goals"));
                matches.add(match);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving played matches: " + e.getMessage());
        }
        return matches;
    }
}

