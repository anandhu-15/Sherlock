package sherlock.model;

/**
 * Plain data object representing one lost or found item.
 * Each field maps directly to a column in the "items" table.
 */
public class Item {

    private int id;
    private String type;        // LOST or FOUND
    private String name;
    private String description;
    private String location;
    private Double latitude;
    private Double longitude;
    private String reporter;
    private String contact;
    private String status;      // LOST, FOUND, MATCHED, RETURNED
    private Integer matchId;    // ID of the matched item (null if unmatched)
    private String token;       // 6-char handover token (null if unmatched)
    private String createdAt;   // ISO-format timestamp

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getReporter() { return reporter; }
    public void setReporter(String reporter) { this.reporter = reporter; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
