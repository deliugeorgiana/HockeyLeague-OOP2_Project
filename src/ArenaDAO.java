import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Arena entity
 */
public class ArenaDAO {
    private final Connection connection;

    public ArenaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Create a new arena in database
     */
    public void create(Arena arena) {
        String sql = "INSERT INTO arena (name, city, capacity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, arena.getName());
            stmt.setString(2, arena.getCity());
            stmt.setInt(3, arena.getCapacity());
            stmt.executeUpdate();
            System.out.println("Arena created: " + arena.getName());
        } catch (SQLException e) {
            System.err.println("Error creating arena: " + e.getMessage());
        }
    }

    /**
     * Find arena by ID
     */
    public Optional<Arena> findById(int id) {
        String sql = "SELECT id, name, city, capacity FROM arena WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Arena arena = new Arena(rs.getString("name"), rs.getString("city"), rs.getInt("capacity"));
                return Optional.of(arena);
            }
        } catch (SQLException e) {
            System.err.println("Error finding arena: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Get all arenas
     */
    public List<Arena> findAll() {
        List<Arena> arenas = new ArrayList<>();
        String sql = "SELECT id, name, city, capacity FROM arena";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Arena arena = new Arena(rs.getString("name"), rs.getString("city"), rs.getInt("capacity"));
                arenas.add(arena);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving arenas: " + e.getMessage());
        }
        return arenas;
    }

    /**
     * Update arena
     */
    public void update(int id, Arena arena) {
        String sql = "UPDATE arena SET name = ?, city = ?, capacity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, arena.getName());
            stmt.setString(2, arena.getCity());
            stmt.setInt(3, arena.getCapacity());
            stmt.setInt(4, id);
            stmt.executeUpdate();
            System.out.println("Arena updated: " + arena.getName());
        } catch (SQLException e) {
            System.err.println("Error updating arena: " + e.getMessage());
        }
    }

    /**
     * Delete arena by ID
     */
    public void delete(int id) {
        String sql = "DELETE FROM arena WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Arena deleted: " + id);
        } catch (SQLException e) {
            System.err.println("Error deleting arena: " + e.getMessage());
        }
    }

    /**
     * Get arena by name
     */
    public Optional<Integer> findIdByName(String name) {
        String sql = "SELECT id FROM arena WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.err.println("Error finding arena by name: " + e.getMessage());
        }
        return Optional.empty();
    }
}

