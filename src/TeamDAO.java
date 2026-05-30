import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Team entity
 */
public class TeamDAO {
    private final Connection connection;

    public TeamDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Create a new team in database
     */
    public void create(Team team) {
        String sql = "INSERT INTO team (id, name, city) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, team.getId());
            stmt.setString(2, team.getName());
            stmt.setString(3, team.getCity());
            stmt.executeUpdate();
            System.out.println("Team created: " + team.getName());
        } catch (SQLException e) {
            System.err.println("Error creating team: " + e.getMessage());
        }
    }

    /**
     * Find team by ID
     */
    public Optional<Team> findById(String id) {
        String sql = "SELECT id, name, city FROM team WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Team team = new Team(rs.getString("id"), rs.getString("name"), rs.getString("city"));
                return Optional.of(team);
            }
        } catch (SQLException e) {
            System.err.println("Error finding team: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Get all teams
     */
    public List<Team> findAll() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT id, name, city FROM team";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Team team = new Team(rs.getString("id"), rs.getString("name"), rs.getString("city"));
                teams.add(team);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving teams: " + e.getMessage());
        }
        return teams;
    }

    /**
     * Update team
     */
    public void update(Team team) {
        String sql = "UPDATE team SET name = ?, city = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, team.getName());
            stmt.setString(2, team.getCity());
            stmt.setString(3, team.getId());
            stmt.executeUpdate();
            System.out.println("Team updated: " + team.getName());
        } catch (SQLException e) {
            System.err.println("Error updating team: " + e.getMessage());
        }
    }

    /**
     * Delete team by ID
     */
    public void delete(String id) {
        String sql = "DELETE FROM team WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            System.out.println("Team deleted: " + id);
        } catch (SQLException e) {
            System.err.println("Error deleting team: " + e.getMessage());
        }
    }

    /**
     * Get team count
     */
    public int count() {
        String sql = "SELECT COUNT(*) as count FROM team";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error counting teams: " + e.getMessage());
        }
        return 0;
    }
}

