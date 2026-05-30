import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Contract entity
 */
public class ContractDAO {
    private final Connection connection;

    public ContractDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Create a new contract in database
     */
    public void create(Contract contract) {
        String sql = "INSERT INTO contract (player_id, team_id, yearly_salary, signed_at, duration_years) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, contract.getPlayer().getId());
            stmt.setString(2, contract.getTeam().getId());
            stmt.setDouble(3, contract.getYearlySalary());
            stmt.setDate(4, java.sql.Date.valueOf(contract.getSignedAt()));
            stmt.setInt(5, contract.getDurationYears());
            stmt.executeUpdate();
            System.out.println("Contract created: " + contract.getPlayer().getFullName() + " - " + contract.getTeam().getName());
        } catch (SQLException e) {
            System.err.println("Error creating contract: " + e.getMessage());
        }
    }

    /**
     * Find contract by ID
     */
    public Optional<Contract> findById(int id) {
        String sql = "SELECT c.player_id, c.team_id, c.yearly_salary, c.signed_at, c.duration_years " +
                "FROM contract c WHERE c.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PlayerDAO playerDAO = new PlayerDAO();
                TeamDAO teamDAO = new TeamDAO();

                Player player = playerDAO.findById(rs.getString("player_id")).orElse(null);
                Team team = teamDAO.findById(rs.getString("team_id")).orElse(null);

                if (player != null && team != null) {
                    Contract contract = new Contract(
                            player,
                            team,
                            rs.getDouble("yearly_salary"),
                            rs.getDate("signed_at").toLocalDate(),
                            rs.getInt("duration_years")
                    );
                    return Optional.of(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding contract: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Get all contracts
     */
    public List<Contract> findAll() {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT c.player_id, c.team_id, c.yearly_salary, c.signed_at, c.duration_years " +
                "FROM contract c";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            PlayerDAO playerDAO = new PlayerDAO();
            TeamDAO teamDAO = new TeamDAO();

            while (rs.next()) {
                Player player = playerDAO.findById(rs.getString("player_id")).orElse(null);
                Team team = teamDAO.findById(rs.getString("team_id")).orElse(null);

                if (player != null && team != null) {
                    Contract contract = new Contract(
                            player,
                            team,
                            rs.getDouble("yearly_salary"),
                            rs.getDate("signed_at").toLocalDate(),
                            rs.getInt("duration_years")
                    );
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving contracts: " + e.getMessage());
        }
        return contracts;
    }

    /**
     * Update contract
     */
    public void update(int id, Contract contract) {
        String sql = "UPDATE contract SET yearly_salary = ?, duration_years = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, contract.getYearlySalary());
            stmt.setInt(2, contract.getDurationYears());
            stmt.setInt(3, id);
            stmt.executeUpdate();
            System.out.println("Contract updated.");
        } catch (SQLException e) {
            System.err.println("Error updating contract: " + e.getMessage());
        }
    }

    /**
     * Delete contract by ID
     */
    public void delete(int id) {
        String sql = "DELETE FROM contract WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Contract deleted: " + id);
        } catch (SQLException e) {
            System.err.println("Error deleting contract: " + e.getMessage());
        }
    }

    /**
     * Get contracts by player ID
     */
    public List<Contract> findByPlayerId(String playerId) {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT c.player_id, c.team_id, c.yearly_salary, c.signed_at, c.duration_years " +
                "FROM contract c WHERE c.player_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, playerId);
            ResultSet rs = stmt.executeQuery();

            PlayerDAO playerDAO = new PlayerDAO();
            TeamDAO teamDAO = new TeamDAO();

            while (rs.next()) {
                Player player = playerDAO.findById(rs.getString("player_id")).orElse(null);
                Team team = teamDAO.findById(rs.getString("team_id")).orElse(null);

                if (player != null && team != null) {
                    Contract contract = new Contract(
                            player,
                            team,
                            rs.getDouble("yearly_salary"),
                            rs.getDate("signed_at").toLocalDate(),
                            rs.getInt("duration_years")
                    );
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding contracts by player: " + e.getMessage());
        }
        return contracts;
    }

    /**
     * Get contracts by team ID
     */
    public List<Contract> findByTeamId(String teamId) {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT c.player_id, c.team_id, c.yearly_salary, c.signed_at, c.duration_years " +
                "FROM contract c WHERE c.team_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            ResultSet rs = stmt.executeQuery();

            PlayerDAO playerDAO = new PlayerDAO();
            TeamDAO teamDAO = new TeamDAO();

            while (rs.next()) {
                Player player = playerDAO.findById(rs.getString("player_id")).orElse(null);
                Team team = teamDAO.findById(rs.getString("team_id")).orElse(null);

                if (player != null && team != null) {
                    Contract contract = new Contract(
                            player,
                            team,
                            rs.getDouble("yearly_salary"),
                            rs.getDate("signed_at").toLocalDate(),
                            rs.getInt("duration_years")
                    );
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding contracts by team: " + e.getMessage());
        }
        return contracts;
    }

    /**
     * Get total salary cost for a team
     */
    public double getTotalTeamSalary(String teamId) {
        String sql = "SELECT SUM(yearly_salary) as total FROM contract WHERE team_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                return total > 0 ? total : 0;
            }
        } catch (SQLException e) {
            System.err.println("Error calculating team salary: " + e.getMessage());
        }
        return 0;
    }
}

