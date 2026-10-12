package sherlock.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the SQLite database connection.
 * Creates the "items" table automatically on first use.
 */
public class Database {

    // ── Change this ONE constant to switch to MySQL or another database ──
    private static final String URL = "jdbc:sqlite:sherlock.db";

    // Returns a new connection to the database.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Creates the items table if it does not already exist.
    public static void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS items (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    type        TEXT NOT NULL,
                    name        TEXT NOT NULL,
                    description TEXT NOT NULL,
                    location    TEXT NOT NULL,
                    latitude    REAL,
                    longitude   REAL,
                    reporter    TEXT NOT NULL,
                    contact     TEXT NOT NULL,
                    status      TEXT NOT NULL,
                    match_id    INTEGER,
                    token       TEXT,
                    created_at  TEXT NOT NULL
                )
                """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create items table", e);
        }
    }
}
