import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Coach entity
 */
public class CoachDAO {
    private final Connection connection;

    public CoachDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Create a new coach in database
     */
    public void create(Coach coach) {
        String personSql = "INSERT INTO person (id, first_name, last_name, age, person_type) VALUES (?, ?, ?, ?, 'COACH')";
        String coachSql = "INSERT INTO coach (id, experience_years, strategy_style) VALUES (?, ?, ?)";

        try (PreparedStatement personStmt = connection.prepareStatement(personSql)) {
            personStmt.setString(1, coach.getId());
            personStmt.setString(2, coach.getFirstName());
            personStmt.setString(3, coach.getLastName());
            personStmt.setInt(4, coach.getAge());
            personStmt.executeUpdate();

            PreparedStatement coachStmt = connection.prepareStatement(coachSql);
            coachStmt.setString(1, coach.getId());
            coachStmt.setInt(2, coach.getExperienceYears());
            coachStmt.setString(3, coach.getStrategyStyle());
            coachStmt.executeUpdate();
            coachStmt.close();

            System.out.println("Coach created: " + coach.getFullName());
        } catch (SQLException e) {
            System.err.println("Error creating coach: " + e.getMessage());
        }
    }

    /**
     * Find coach by ID
     */
    public Optional<Coach> findById(String id) {
        String sql = "SELECT p.id, p.first_name, p.last_name, p.age, c.experience_years, c.strategy_style " +
                "FROM person p JOIN coach c ON p.id = c.id WHERE p.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Coach coach = new Coach(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getInt("experience_years"),
                        rs.getString("strategy_style")
                );
                return Optional.of(coach);
            }
        } catch (SQLException e) {
            System.err.println("Error finding coach: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Get all coaches
     */
    public List<Coach> findAll() {
        List<Coach> coaches = new ArrayList<>();
        String sql = "SELECT p.id, p.first_name, p.last_name, p.age, c.experience_years, c.strategy_style " +
                "FROM person p JOIN coach c ON p.id = c.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Coach coach = new Coach(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getInt("experience_years"),
                        rs.getString("strategy_style")
                );
                coaches.add(coach);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving coaches: " + e.getMessage());
        }
        return coaches;
    }

    /**
     * Update coach
     */
    public void update(Coach coach) {
        String sql = "UPDATE coach SET experience_years = ?, strategy_style = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, coach.getExperienceYears());
            stmt.setString(2, coach.getStrategyStyle());
            stmt.setString(3, coach.getId());
            stmt.executeUpdate();
            System.out.println("Coach updated: " + coach.getFullName());
        } catch (SQLException e) {
            System.err.println("Error updating coach: " + e.getMessage());
        }
    }

    /**
     * Delete coach by ID
     */
    public void delete(String id) {
        String sql = "DELETE FROM person WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            System.out.println("Coach deleted: " + id);
        } catch (SQLException e) {
            System.err.println("Error deleting coach: " + e.getMessage());
        }
    }

    /**
     * Assign coach to team
     */
    public void assignToTeam(String coachId, String teamId) {
        String personSql = "UPDATE person SET team_id = ? WHERE id = ?";
        String teamCoachSql = "INSERT INTO team_coach (team_id, coach_id) VALUES (?, ?) " +
                "ON CONFLICT (team_id) DO UPDATE SET coach_id = ?";

        try {
            // Update person table
            PreparedStatement personStmt = connection.prepareStatement(personSql);
            personStmt.setString(1, teamId);
            personStmt.setString(2, coachId);
            personStmt.executeUpdate();
            personStmt.close();

            // Insert/update team_coach relationship
            PreparedStatement teamCoachStmt = connection.prepareStatement(teamCoachSql);
            teamCoachStmt.setString(1, teamId);
            teamCoachStmt.setString(2, coachId);
            teamCoachStmt.setString(3, coachId);
            teamCoachStmt.executeUpdate();
            teamCoachStmt.close();

            System.out.println("Coach assigned to team.");
        } catch (SQLException e) {
            System.err.println("Error assigning coach to team: " + e.getMessage());
        }
    }

    /**
     * Get coach by team ID
     */
    public Optional<Coach> findByTeamId(String teamId) {
        String sql = "SELECT p.id, p.first_name, p.last_name, p.age, c.experience_years, c.strategy_style " +
                "FROM person p JOIN coach c ON p.id = c.id JOIN team_coach tc ON c.id = tc.coach_id " +
                "WHERE tc.team_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Coach coach = new Coach(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getInt("experience_years"),
                        rs.getString("strategy_style")
                );
                return Optional.of(coach);
            }
        } catch (SQLException e) {
            System.err.println("Error finding coach by team: " + e.getMessage());
        }
        return Optional.empty();
    }
}

