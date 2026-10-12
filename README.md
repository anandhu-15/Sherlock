# Sherlock — Campus Lost & Found

A simple lost-and-found web application for **NSS College of Engineering, Palakkad**.
Built with Java 17, Servlets, JSP (JSTL), JDBC (SQLite), and Maven.

---

## How to Run

```bash
# 1. Make sure Java 17+ and Maven are installed
java -version
mvn -version

# 2. Clone/download, then start the server
cd sherlock
mvn jetty:run

# 3. Open in your browser
# http://localhost:8080
```

The SQLite database file (`sherlock.db`) is created automatically in the
project root on first start. No setup needed.

**Admin login password:** `sherlock2024`

---

## Database Table

```sql
CREATE TABLE items (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    type        TEXT NOT NULL,        -- LOST or FOUND
    name        TEXT NOT NULL,
    description TEXT NOT NULL,
    location    TEXT NOT NULL,        -- campus location from dropdown
    latitude    REAL,                 -- optional GPS lat
    longitude   REAL,                 -- optional GPS lng
    reporter    TEXT NOT NULL,
    contact     TEXT NOT NULL,
    status      TEXT NOT NULL,        -- LOST, FOUND, MATCHED, RETURNED
    match_id    INTEGER,              -- ID of the matched item (nullable)
    token       TEXT,                 -- 6-char handover token (nullable)
    created_at  TEXT NOT NULL          -- timestamp (yyyy-MM-dd HH:mm)
);
```

---

## File-by-File Explanation

### Java Source (`src/main/java/sherlock/`)

| File | What it does |
|------|-------------|
| **model/Item.java** | Plain data class (POJO). One field per database column, with getters and setters. No logic — just carries data between layers. |
| **db/Database.java** | Manages the SQLite connection. Has one constant (`URL`) that you change to switch databases. The `initialize()` method creates the items table on first run. |
| **db/ItemDao.java** | Data Access Object — all SQL lives here. Methods: `insert`, `findRecent`, `search`, `findByStatusAndType`, `findById`, `setMatched`, `markReturned`, `countByStatus`. Every query uses `PreparedStatement`. |
| **service/MatchService.java** | Auto-matching algorithm. When a new item is saved, it compares keywords (name + description) with opposite-type items. Score = % shared keywords + 15 bonus for same location. If score ≥ 75 → match. |
| **web/HomeServlet.java** | Handles `/home`. Calls `Database.initialize()` once on startup. Loads counts and 5 recent items, forwards to `home.jsp`. |
| **web/ReportServlet.java** | Handles `/report`. GET shows the form. POST reads form fields, validates, saves the item, runs auto-matching, and forwards to `confirm.jsp`. |
| **web/ItemsServlet.java** | Handles `/items`. Passes search query and status filter to the DAO, forwards the result list to `items.jsp`. |
| **web/LoginServlet.java** | Handles `/admin/login`. Compares input against a hardcoded password constant. On success, sets `admin=true` in `HttpSession`. |
| **web/HandoverServlet.java** | Handles `/admin/handover`. Takes the 6-char token, calls `markReturned()` in the DAO. Shows success or error. |
| **web/AuthFilter.java** | Servlet filter on `/admin/*`. Checks if session has the `admin` attribute. If not → redirects to login. Lets `/admin/login` through. |

### Web Resources (`src/main/webapp/`)

| File | What it does |
|------|-------------|
| **index.jsp** | Welcome file — just forwards `/` to the HomeServlet. |
| **home.jsp** | Home page — intro text, 4 stat cards (LOST/FOUND/MATCHED/RETURNED), and a table of 5 recent items. |
| **report.jsp** | Report form — type dropdown, item name, description, location dropdown, reporter name, contact, geolocation button. |
| **confirm.jsp** | Shown after submitting a report. Displays the match token if the item was LOST and a match was found. |
| **items.jsp** | Lists all items in a table with search and filter. Shows "Map" link if GPS coordinates exist. |
| **login.jsp** | Simple password form for admin login. |
| **handover.jsp** | Admin enters the 6-char token to mark items as RETURNED. |
| **css/style.css** | Single stylesheet. White/grey background, blue (#2563EB) accent, system font, max-width 900px, mobile-responsive. |

---

## How a Request Flows

```
Browser                Servlet               DAO / Service          JSP
───────                ───────               ─────────────          ───
GET /items?q=bottle
        ──────────►  ItemsServlet.doGet()
                            │
                            ├──► ItemDao.search("bottle", null)
                            │         │
                            │         ├──► Database.getConnection()
                            │         ├──► PreparedStatement (SQL)
                            │         └──► returns List<Item>
                            │
                            ├──► req.setAttribute("items", list)
                            └──► forward to items.jsp
                                                    │
                                                    ├──► <c:forEach> renders table
                                                    └──► HTML response ◄────── Browser
```

1. **Browser** sends an HTTP request (e.g. `GET /items?q=bottle`).
2. **Servlet** reads the parameters, calls the DAO/Service.
3. **DAO** builds a `PreparedStatement`, queries SQLite, returns `List<Item>`.
4. **Servlet** puts the data into `request.setAttribute()` and forwards to a JSP.
5. **JSP** uses JSTL `<c:forEach>` and `<c:out>` to render HTML safely.
6. **Browser** receives the HTML page.

---

## 10 Likely Viva Questions and Answers

### 1. What is a Servlet?
A Servlet is a Java class that runs on the server and handles HTTP requests
(GET, POST). It reads the request, does some processing, and sends back a
response — usually by forwarding to a JSP page.

### 2. What is JSP and how is it different from a Servlet?
JSP (JavaServer Pages) is an HTML file that can contain Java-like tags. At
runtime, the server compiles it into a Servlet. We use JSP for the **view**
(HTML) and Servlets for the **controller** (logic).

### 3. What is JSTL? Why do you use `<c:out>`?
JSTL (JSP Standard Tag Library) provides tags like `<c:forEach>`, `<c:if>`,
and `<c:out>` so we don't need Java scriptlets in JSP. `<c:out>` escapes
special HTML characters to prevent XSS (Cross-Site Scripting) attacks.

### 4. What is a PreparedStatement and why not use string concatenation?
`PreparedStatement` separates SQL from data. The database treats parameters as
data, never as SQL code. String concatenation is vulnerable to **SQL injection**
— a user could type `'; DROP TABLE items; --` and destroy your data.

### 5. How does the auto-matching algorithm work?
When a new item is reported, we extract keywords from its name and description
(ignoring stop words like "the", "a"). We compare these with each item of the
opposite type. The score is the percentage of shared keywords, plus a 15-point
bonus if the campus location matches (capped at 100). If the best score is ≥ 75,
both items are set to MATCHED and a 6-character token is generated.

### 6. What is HttpSession and how do you use it for login?
`HttpSession` is server-side storage tied to a browser cookie (`JSESSIONID`).
On successful login, we store `session.setAttribute("admin", true)`. The
`AuthFilter` checks this attribute on every `/admin/*` request.

### 7. What does the AuthFilter do?
It's a Servlet Filter mapped to `/admin/*`. Before any admin page loads, the
filter checks if the session has the "admin" attribute. If not, it redirects
to `/admin/login`. This avoids repeating the login check in every servlet.

### 8. Why SQLite? Can you switch to MySQL?
SQLite needs zero setup — it's just a file. For a mini-project, that's ideal.
To switch to MySQL, we change one constant in `Database.java` (the JDBC URL)
and swap the Maven dependency from `sqlite-jdbc` to `mysql-connector-j`.

### 9. What is the MVC pattern in this project?
- **Model**: `Item.java` — the data.
- **View**: JSP files — the HTML pages.
- **Controller**: Servlets — read request, call DAO, forward to JSP.
The DAO is a helper that isolates all SQL from the rest of the code.

### 10. How do you prevent XSS in this project?
Every piece of user-entered text is displayed using `<c:out value="..."/>` in
JSP. This escapes characters like `<`, `>`, `&`, and `"` so a user cannot
inject malicious HTML or JavaScript into the page.
