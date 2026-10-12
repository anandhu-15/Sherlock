package sherlock.web;

import sherlock.db.ItemDao;
import sherlock.model.Item;
import sherlock.service.MatchService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles the report form (/report).
 * GET shows the form; POST saves the item and attempts auto-matching.
 */
public class ReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/report.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            // Read form fields into an Item object.
            Item item = new Item();
            item.setType(req.getParameter("type"));
            item.setName(req.getParameter("name").trim());
            item.setDescription(req.getParameter("description").trim());
            item.setLocation(req.getParameter("location"));
            item.setReporter(req.getParameter("reporter").trim());
            item.setContact(req.getParameter("contact").trim());
            item.setStatus(item.getType());   // initial status = LOST or FOUND

            // Parse optional GPS coordinates.
            String lat = req.getParameter("latitude");
            String lng = req.getParameter("longitude");
            if (lat != null && !lat.isBlank()) item.setLatitude(Double.parseDouble(lat));
            if (lng != null && !lng.isBlank()) item.setLongitude(Double.parseDouble(lng));

            // Simple server-side validation.
            if (item.getName().isEmpty() || item.getDescription().isEmpty()
                    || item.getReporter().isEmpty() || item.getContact().isEmpty()
                    || item.getLocation() == null || item.getLocation().isEmpty()) {
                req.setAttribute("error", "All fields are required.");
                req.getRequestDispatcher("/report.jsp").forward(req, resp);
                return;
            }

            // Save to database.
            ItemDao dao = new ItemDao();
            int id = dao.insert(item);
            item.setId(id);

            // Try to auto-match with an opposite-type item.
            MatchService matchService = new MatchService();
            String token = matchService.tryMatch(item);

            req.setAttribute("item", item);
            req.setAttribute("token", token);
            req.getRequestDispatcher("/confirm.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
