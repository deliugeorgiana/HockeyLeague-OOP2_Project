import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Referee entity
 */
public class RefereeDAO {
    private final Connection connection;

    public RefereeDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Create a new referee in database
     */
    public void create(Referee referee) {
        String personSql = "INSERT INTO person (id, first_name, last_name, age, person_type) VALUES (?, ?, ?, ?, 'REFEREE')";
        String refereeSql = "INSERT INTO referee (id, license_level, matches_officiated) VALUES (?, ?, 0)";

        try (PreparedStatement personStmt = connection.prepareStatement(personSql)) {
            personStmt.setString(1, referee.getId());
            personStmt.setString(2, referee.getFirstName());
            personStmt.setString(3, referee.getLastName());
            personStmt.setInt(4, referee.getAge());
            personStmt.executeUpdate();

            PreparedStatement refereeStmt = connection.prepareStatement(refereeSql);
            refereeStmt.setString(1, referee.getId());
            refereeStmt.setString(2, referee.getLicenseLevel());
            refereeStmt.executeUpdate();
            refereeStmt.close();

            System.out.println("Referee created: " + referee.getFullName());
        } catch (SQLException e) {
            System.err.println("Error creating referee: " + e.getMessage());
        }
    }

    /**
     * Find referee by ID
     */
    public Optional<Referee> findById(String id) {
        String sql = "SELECT p.id, p.first_name, p.last_name, p.age, r.license_level, r.matches_officiated " +
                "FROM person p JOIN referee r ON p.id = r.id WHERE p.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Referee referee = new Referee(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("license_level")
                );
                for (int i = 0; i < rs.getInt("matches_officiated"); i++) {
                    referee.addOfficiatedMatch();
                }
                return Optional.of(referee);
            }
        } catch (SQLException e) {
            System.err.println("Error finding referee: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Get all referees
     */
    public List<Referee> findAll() {
        List<Referee> referees = new ArrayList<>();
        String sql = "SELECT p.id, p.first_name, p.last_name, p.age, r.license_level, r.matches_officiated " +
                "FROM person p JOIN referee r ON p.id = r.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Referee referee = new Referee(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("license_level")
                );
                for (int i = 0; i < rs.getInt("matches_officiated"); i++) {
                    referee.addOfficiatedMatch();
                }
                referees.add(referee);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving referees: " + e.getMessage());
        }
        return referees;
    }

    /**
     * Update referee
     */
    public void update(Referee referee) {
        String sql = "UPDATE referee SET license_level = ?, matches_officiated = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, referee.getLicenseLevel());
            stmt.setInt(2, referee.getMatchesOfficiated());
            stmt.setString(3, referee.getId());
            stmt.executeUpdate();
            System.out.println("Referee updated: " + referee.getFullName());
        } catch (SQLException e) {
            System.err.println("Error updating referee: " + e.getMessage());
        }
    }

    /**
     * Delete referee by ID
     */
    public void delete(String id) {
        String sql = "DELETE FROM person WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            System.out.println("Referee deleted: " + id);
        } catch (SQLException e) {
            System.err.println("Error deleting referee: " + e.getMessage());
        }
    }

    /**
     * Increment matches officiated
     */
    public void addOfficialMatch(String refereeId) {
        String sql = "UPDATE referee SET matches_officiated = matches_officiated + 1 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, refereeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error incrementing referee matches: " + e.getMessage());
        }
    }
}

