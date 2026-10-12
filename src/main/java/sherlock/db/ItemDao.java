package sherlock.db;

import sherlock.model.Item;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Data Access Object for the items table.
 * Every method uses PreparedStatement to prevent SQL injection.
 */
public class ItemDao {

    // Inserts a new item and returns its auto-generated ID.
    public int insert(Item item) throws SQLException {
        String sql = "INSERT INTO items (type, name, description, location, latitude, longitude, reporter, contact, status, created_at) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getType());
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setString(4, item.getLocation());
            ps.setObject(5, item.getLatitude());
            ps.setObject(6, item.getLongitude());
            ps.setString(7, item.getReporter());
            ps.setString(8, item.getContact());
            ps.setString(9, item.getStatus());
            ps.setString(10, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        }
    }

    // Returns the N most recent items, newest first.
    public List<Item> findRecent(int limit) throws SQLException {
        String sql = "SELECT * FROM items ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            return mapResults(ps.executeQuery());
        }
    }

    // Searches items by text (name/description) and optional status filter.
    public List<Item> search(String query, String status) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM items WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (query != null && !query.isBlank()) {
            sql.append(" AND (LOWER(name) LIKE ? OR LOWER(description) LIKE ?)");
            String like = "%" + query.toLowerCase() + "%";
            params.add(like); params.add(like);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        sql.append(" ORDER BY created_at DESC");
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            return mapResults(ps.executeQuery());
        }
    }

    // Returns all items with a given status and type (used for matching).
    public List<Item> findByStatusAndType(String status, String type) throws SQLException {
        String sql = "SELECT * FROM items WHERE status = ? AND type = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, type);
            return mapResults(ps.executeQuery());
        }
    }

    // Returns a single item by its ID, or null if not found.
    public Item findById(int id) throws SQLException {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            List<Item> results = mapResults(ps.executeQuery());
            return results.isEmpty() ? null : results.get(0);
        }
    }

    // Links two items as MATCHED and stores the handover token on both.
    public void setMatched(int id1, int id2, String token) throws SQLException {
        String sql = "UPDATE items SET status='MATCHED', match_id=?, token=? WHERE id=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id2); ps.setString(2, token); ps.setInt(3, id1);
            ps.executeUpdate();
            ps.setInt(1, id1); ps.setString(2, token); ps.setInt(3, id2);
            ps.executeUpdate();
        }
    }

    // Marks both items in a match as RETURNED using the token.
    public boolean markReturned(String token) throws SQLException {
        String sql = "UPDATE items SET status='RETURNED' WHERE token=? AND status='MATCHED'";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        }
    }

    // Counts items grouped by status (e.g. {LOST=3, FOUND=2, MATCHED=1}).
    public Map<String, Integer> countByStatus() throws SQLException {
        String sql = "SELECT status, COUNT(*) AS cnt FROM items GROUP BY status";
        Map<String, Integer> counts = new HashMap<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) counts.put(rs.getString("status"), rs.getInt("cnt"));
        }
        return counts;
    }

    // Helper: converts a ResultSet into a list of Item objects.
    private List<Item> mapResults(ResultSet rs) throws SQLException {
        List<Item> items = new ArrayList<>();
        while (rs.next()) {
            Item item = new Item();
            item.setId(rs.getInt("id"));
            item.setType(rs.getString("type"));
            item.setName(rs.getString("name"));
            item.setDescription(rs.getString("description"));
            item.setLocation(rs.getString("location"));
            double lat = rs.getDouble("latitude");
            item.setLatitude(rs.wasNull() ? null : lat);
            double lng = rs.getDouble("longitude");
            item.setLongitude(rs.wasNull() ? null : lng);
            item.setReporter(rs.getString("reporter"));
            item.setContact(rs.getString("contact"));
            item.setStatus(rs.getString("status"));
            int matchId = rs.getInt("match_id");
            item.setMatchId(rs.wasNull() ? null : matchId);
            item.setToken(rs.getString("token"));
            item.setCreatedAt(rs.getString("created_at"));
            items.add(item);
        }
        return items;
    }
}
