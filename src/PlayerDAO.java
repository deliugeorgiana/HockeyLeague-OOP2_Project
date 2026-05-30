import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Player entity
 */
public class PlayerDAO {
    private final Connection connection;

    public PlayerDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Create a new player in database
     */
    public void create(Player player) {
        String personSql = "INSERT INTO person (id, first_name, last_name, age, person_type) VALUES (?, ?, ?, ?, 'PLAYER')";
        String playerSql = "INSERT INTO player (id, position, jersey_number, goals, assists) VALUES (?, ?, ?, 0, 0)";

        try (PreparedStatement personStmt = connection.prepareStatement(personSql)) {
            personStmt.setString(1, player.getId());
            personStmt.setString(2, player.getFirstName());
            personStmt.setString(3, player.getLastName());
            personStmt.setInt(4, player.getAge());
            personStmt.executeUpdate();

            PreparedStatement playerStmt = connection.prepareStatement(playerSql);
            playerStmt.setString(1, player.getId());
            playerStmt.setString(2, player.getPosition());
            playerStmt.setInt(3, player.getJerseyNumber());
            playerStmt.executeUpdate();
            playerStmt.close();

            System.out.println("Player created: " + player.getFullName());
        } catch (SQLException e) {
            System.err.println("Error creating player: " + e.getMessage());
        }
    }

    /**
     * Find player by ID
     */
    public Optional<Player> findById(String id) {
        String sql = "SELECT p.id, p.first_name, p.last_name, p.age, pl.position, pl.jersey_number, pl.goals, pl.assists " +
                "FROM person p JOIN player pl ON p.id = pl.id WHERE p.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Player player = new Player(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("position"),
                        rs.getInt("jersey_number")
                );
                // Set goals and assists
                for (int i = 0; i < rs.getInt("goals"); i++) {
                    player.registerGoal();
                }
                for (int i = 0; i < rs.getInt("assists"); i++) {
                    player.registerAssist();
                }
                return Optional.of(player);
            }
        } catch (SQLException e) {
            System.err.println("Error finding player: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Get all players
     */
    public List<Player> findAll() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT p.id, p.first_name, p.last_name, p.age, pl.position, pl.jersey_number, pl.goals, pl.assists " +
                "FROM person p JOIN player pl ON p.id = pl.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Player player = new Player(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("position"),
                        rs.getInt("jersey_number")
                );
                for (int i = 0; i < rs.getInt("goals"); i++) {
                    player.registerGoal();
                }
                for (int i = 0; i < rs.getInt("assists"); i++) {
                    player.registerAssist();
                }
                players.add(player);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving players: " + e.getMessage());
        }
        return players;
    }

    /**
     * Update player statistics
     */
    public void updateStats(String playerId, int goals, int assists) {
        String sql = "UPDATE player SET goals = ?, assists = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, goals);
            stmt.setInt(2, assists);
            stmt.setString(3, playerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating player stats: " + e.getMessage());
        }
    }

    /**
     * Delete player by ID
     */
    public void delete(String id) {
        String sql = "DELETE FROM person WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            System.out.println("Player deleted: " + id);
        } catch (SQLException e) {
            System.err.println("Error deleting player: " + e.getMessage());
        }
    }

    /**
     * Get players by team ID
     */
    public List<Player> findByTeamId(String teamId) {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT p.id, p.first_name, p.last_name, p.age, pl.position, pl.jersey_number, pl.goals, pl.assists " +
                "FROM person p JOIN player pl ON p.id = pl.id WHERE p.team_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Player player = new Player(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("position"),
                        rs.getInt("jersey_number")
                );
                for (int i = 0; i < rs.getInt("goals"); i++) {
                    player.registerGoal();
                }
                for (int i = 0; i < rs.getInt("assists"); i++) {
                    player.registerAssist();
                }
                players.add(player);
            }
        } catch (SQLException e) {
            System.err.println("Error finding players by team: " + e.getMessage());
        }
        return players;
    }

    /**
     * Assign player to team
     */
    public void assignToTeam(String playerId, String teamId) {
        String sql = "UPDATE person SET team_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            stmt.setString(2, playerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error assigning player to team: " + e.getMessage());
        }
    }
}

