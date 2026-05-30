import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Utility class for initializing the database schema
 */
public class DatabaseInitializer {

    /**
     * Initialize the database with all required tables
     */
    public static void initializeDatabase() {
        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (Statement stmt = connection.createStatement()) {
            // Create tables ONLY if they don't exist (no reset on every run)

            stmt.addBatch("CREATE TABLE IF NOT EXISTS arena (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "city VARCHAR(100) NOT NULL," +
                    "capacity INT NOT NULL" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS team (" +
                    "id VARCHAR(20) PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "city VARCHAR(100) NOT NULL" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS person (" +
                    "id VARCHAR(20) PRIMARY KEY," +
                    "first_name VARCHAR(100) NOT NULL," +
                    "last_name VARCHAR(100) NOT NULL," +
                    "age INT NOT NULL," +
                    "person_type VARCHAR(20) NOT NULL," +
                    "team_id VARCHAR(20)," +
                    "FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE SET NULL" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS player (" +
                    "id VARCHAR(20) PRIMARY KEY," +
                    "position VARCHAR(50) NOT NULL," +
                    "jersey_number INT NOT NULL," +
                    "goals INT DEFAULT 0," +
                    "assists INT DEFAULT 0," +
                    "FOREIGN KEY (id) REFERENCES person(id) ON DELETE CASCADE" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS coach (" +
                    "id VARCHAR(20) PRIMARY KEY," +
                    "experience_years INT NOT NULL," +
                    "strategy_style VARCHAR(100) NOT NULL," +
                    "FOREIGN KEY (id) REFERENCES person(id) ON DELETE CASCADE" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS referee (" +
                    "id VARCHAR(20) PRIMARY KEY," +
                    "license_level VARCHAR(50) NOT NULL," +
                    "matches_officiated INT DEFAULT 0," +
                    "FOREIGN KEY (id) REFERENCES person(id) ON DELETE CASCADE" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS team_coach (" +
                    "team_id VARCHAR(20) PRIMARY KEY," +
                    "coach_id VARCHAR(20) NOT NULL," +
                    "FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (coach_id) REFERENCES coach(id) ON DELETE CASCADE" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS contract (" +
                    "id SERIAL PRIMARY KEY," +
                    "player_id VARCHAR(20) NOT NULL," +
                    "team_id VARCHAR(20) NOT NULL," +
                    "yearly_salary DOUBLE PRECISION NOT NULL," +
                    "signed_at DATE NOT NULL," +
                    "duration_years INT NOT NULL," +
                    "FOREIGN KEY (player_id) REFERENCES player(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS league_season (" +
                    "id SERIAL PRIMARY KEY," +
                    "season_label VARCHAR(100) NOT NULL," +
                    "start_year INT NOT NULL," +
                    "end_year INT NOT NULL" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS match (" +
                    "id VARCHAR(20) PRIMARY KEY," +
                    "home_team_id VARCHAR(20) NOT NULL," +
                    "away_team_id VARCHAR(20) NOT NULL," +
                    "arena_id INT NOT NULL," +
                    "match_date DATE NOT NULL," +
                    "is_played BOOLEAN DEFAULT FALSE," +
                    "home_goals INT DEFAULT 0," +
                    "away_goals INT DEFAULT 0," +
                    "FOREIGN KEY (home_team_id) REFERENCES team(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (away_team_id) REFERENCES team(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (arena_id) REFERENCES arena(id) ON DELETE CASCADE" +
                    ")");

            stmt.addBatch("CREATE TABLE IF NOT EXISTS standing_entry (" +
                    "id SERIAL PRIMARY KEY," +
                    "team_id VARCHAR(20) NOT NULL UNIQUE," +
                    "matches_played INT DEFAULT 0," +
                    "wins INT DEFAULT 0," +
                    "draws INT DEFAULT 0," +
                    "losses INT DEFAULT 0," +
                    "goals_for INT DEFAULT 0," +
                    "goals_against INT DEFAULT 0," +
                    "points INT DEFAULT 0," +
                    "FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE" +
                    ")");

            // Create indexes
            stmt.addBatch("CREATE INDEX IF NOT EXISTS idx_match_home_team ON match(home_team_id)");
            stmt.addBatch("CREATE INDEX IF NOT EXISTS idx_match_away_team ON match(away_team_id)");
            stmt.addBatch("CREATE INDEX IF NOT EXISTS idx_contract_player ON contract(player_id)");
            stmt.addBatch("CREATE INDEX IF NOT EXISTS idx_contract_team ON contract(team_id)");
            stmt.addBatch("CREATE INDEX IF NOT EXISTS idx_person_team ON person(team_id)");
            stmt.addBatch("CREATE INDEX IF NOT EXISTS idx_standing_team ON standing_entry(team_id)");

            // Demo seed data

            // Arenas
            stmt.addBatch("INSERT INTO arena (name, city, capacity) VALUES" +
                    "('Patinoarul Olimpic', 'Brasov', 3000)," +
                    "('Tiriac Arena', 'Bucuresti', 2500)," +
                    "('Patinoarul Miercurea Ciuc', 'Miercurea Ciuc', 2000)," +
                    "('Gheorgheni Arena', 'Gheorgheni', 1500)" +
                    " ON CONFLICT DO NOTHING");

            // Teams
            stmt.addBatch("INSERT INTO team (id, name, city) VALUES" +
                    "('TM-BV', 'Brasov Wolves', 'Brasov')," +
                    "('TM-B', 'Bucuresti Capitals', 'Bucuresti')," +
                    "('TM-MC', 'Ciuc Bears', 'Miercurea Ciuc')," +
                    "('TM-GH', 'Gheorgheni Knights', 'Gheorgheni')" +
                    " ON CONFLICT (id) DO NOTHING");

            // Season
            stmt.addBatch("INSERT INTO league_season (season_label, start_year, end_year)" +
                    " SELECT 'Winter Season 2026', 2026, 2027" +
                    " WHERE NOT EXISTS (SELECT 1 FROM league_season WHERE season_label = 'Winter Season 2026')");

            // People
            stmt.addBatch("INSERT INTO person (id, first_name, last_name, age, person_type, team_id) VALUES" +
                    "('P-01', 'Andrei', 'Popescu', 25, 'PLAYER', 'TM-BV')," +
                    "('P-02', 'Mihai', 'Ionescu', 28, 'PLAYER', 'TM-BV')," +
                    "('P-03', 'Alexandru', 'Radu', 22, 'PLAYER', 'TM-B')," +
                    "('P-04', 'Cristian', 'Marin', 30, 'PLAYER', 'TM-B')," +
                    "('P-05', 'Attila', 'Kovacs', 26, 'PLAYER', 'TM-MC')," +
                    "('P-06', 'Laszlo', 'Nagy', 29, 'PLAYER', 'TM-MC')," +
                    "('P-07', 'Istvan', 'Szabo', 24, 'PLAYER', 'TM-GH')," +
                    "('P-08', 'Gabor', 'Toth', 27, 'PLAYER', 'TM-GH')," +
                    "('P-09', 'Vlad', 'Nistor', 23, 'PLAYER', 'TM-BV')," +
                    "('P-10', 'George', 'Dinu', 25, 'PLAYER', 'TM-B')," +
                    "('C-01', 'Florin', 'Vasile', 45, 'COACH', 'TM-BV')," +
                    "('C-02', 'Ion', 'Gheorghe', 50, 'COACH', 'TM-B')," +
                    "('C-03', 'Zoltan', 'Balazs', 48, 'COACH', 'TM-MC')," +
                    "('C-04', 'Miklos', 'Farkas', 42, 'COACH', 'TM-GH')," +
                    "('R-01', 'Nicolae', 'Dan', 38, 'REFEREE', NULL)," +
                    "('R-02', 'Bogdan', 'Stan', 35, 'REFEREE', NULL)" +
                    " ON CONFLICT (id) DO NOTHING");

            // Players
            stmt.addBatch("INSERT INTO player (id, position, jersey_number, goals, assists) VALUES" +
                    "('P-01', 'Forward', 10, 5, 3)," +
                    "('P-02', 'Defense', 4, 1, 4)," +
                    "('P-03', 'Forward', 9, 4, 2)," +
                    "('P-04', 'Goalie', 1, 0, 0)," +
                    "('P-05', 'Forward', 11, 6, 5)," +
                    "('P-06', 'Defense', 5, 2, 1)," +
                    "('P-07', 'Center', 19, 3, 3)," +
                    "('P-08', 'Goalie', 30, 0, 1)," +
                    "('P-09', 'Forward', 88, 2, 1)," +
                    "('P-10', 'Defense', 22, 0, 3)" +
                    " ON CONFLICT (id) DO NOTHING");

            // Coaches
            stmt.addBatch("INSERT INTO coach (id, experience_years, strategy_style) VALUES" +
                    "('C-01', 10, 'Offensive / Aggressive pressing')," +
                    "('C-02', 15, 'Defensive / Counterattack')," +
                    "('C-03', 12, 'Balanced / Puck control')," +
                    "('C-04', 8, 'Physical / Strong forecheck')" +
                    " ON CONFLICT (id) DO NOTHING");

            // Referees
            stmt.addBatch("INSERT INTO referee (id, license_level, matches_officiated) VALUES" +
                    "('R-01', 'International A', 120)," +
                    "('R-02', 'National', 45)" +
                    " ON CONFLICT (id) DO NOTHING");

            // Team-coach link
            stmt.addBatch("INSERT INTO team_coach (team_id, coach_id) VALUES" +
                    "('TM-BV', 'C-01')," +
                    "('TM-B', 'C-02')," +
                    "('TM-MC', 'C-03')," +
                    "('TM-GH', 'C-04')" +
                    " ON CONFLICT (team_id) DO NOTHING");

            // Contracts
            stmt.addBatch("INSERT INTO contract (player_id, team_id, yearly_salary, signed_at, duration_years)" +
                    " SELECT v.player_id, v.team_id, v.yearly_salary, v.signed_at, v.duration_years" +
                    " FROM (VALUES" +
                    " ('P-01','TM-BV',45000.00, DATE '2026-01-15', 2)," +
                    " ('P-02','TM-BV',38000.00, DATE '2025-06-15', 3)," +
                    " ('P-03','TM-B', 42000.00, DATE '2026-02-10', 1)," +
                    " ('P-04','TM-B', 50000.00, DATE '2024-05-20', 4)," +
                    " ('P-05','TM-MC',48000.00, DATE '2026-01-10', 2)," +
                    " ('P-06','TM-MC',35000.00, DATE '2025-09-01', 2)," +
                    " ('P-07','TM-GH',30000.00, DATE '2026-01-20', 1)," +
                    " ('P-08','TM-GH',32000.00, DATE '2025-11-11', 3)," +
                    " ('P-09','TM-BV',28000.00, DATE '2026-03-01', 2)," +
                    " ('P-10','TM-B', 31000.00, DATE '2026-02-15', 2)" +
                    ") AS v(player_id, team_id, yearly_salary, signed_at, duration_years)" +
                    " WHERE NOT EXISTS (" +
                    "   SELECT 1 FROM contract c" +
                    "   WHERE c.player_id = v.player_id AND c.team_id = v.team_id AND c.signed_at = v.signed_at" +
                    " )");

            // Matches
            stmt.addBatch("INSERT INTO match (id, home_team_id, away_team_id, arena_id, match_date, is_played, home_goals, away_goals) VALUES" +
                    // Played matches (is_played = TRUE)
                    "('M-001', 'TM-BV', 'TM-B',  1, DATE '2026-10-05', TRUE,  3, 1)," +
                    "('M-002', 'TM-MC', 'TM-GH', 3, DATE '2026-10-06', TRUE,  2, 2)," +
                    "('M-003', 'TM-B',  'TM-MC', 2, DATE '2026-10-12', TRUE,  0, 2)," +
                    "('M-004', 'TM-GH', 'TM-BV', 4, DATE '2026-10-13', TRUE,  1, 4)," +

                    // Scheduled matches (is_played = FALSE)
                    "('M-005', 'TM-BV', 'TM-MC', 1, DATE '2026-11-01', FALSE, 0, 0)," +
                    "('M-006', 'TM-B',  'TM-GH', 2, DATE '2026-11-03', FALSE, 0, 0)," +
                    "('M-007', 'TM-MC', 'TM-B',  3, DATE '2026-11-15', FALSE, 0, 0)," +
                    "('M-008', 'TM-GH', 'TM-B',  4, DATE '2026-11-20', FALSE, 0, 0)," +
                    "('M-009', 'TM-BV', 'TM-GH', 1, DATE '2026-12-05', FALSE, 0, 0)," +
                    "('M-010', 'TM-MC', 'TM-BV', 3, DATE '2026-12-12', FALSE, 0, 0)" +
                    " ON CONFLICT (id) DO NOTHING");

            // Standings
            stmt.addBatch("INSERT INTO standing_entry (team_id, matches_played, wins, draws, losses, goals_for, goals_against, points) VALUES" +
                    "('TM-BV', 2, 2, 0, 0, 7, 2, 6)," +
                    "('TM-MC', 2, 1, 1, 0, 4, 2, 4)," +
                    "('TM-GH', 2, 0, 1, 1, 3, 6, 1)," +
                    "('TM-B',  2, 0, 0, 2, 1, 5, 0)" +
                    " ON CONFLICT (team_id) DO NOTHING");

            stmt.executeBatch();
            System.out.println("Database schema ensured and demo data initialized.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}