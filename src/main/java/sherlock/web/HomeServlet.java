package sherlock.web;

import sherlock.db.Database;
import sherlock.db.ItemDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles the home page (/).
 * Shows item counts by status and the 5 most recent reports.
 */
public class HomeServlet extends HttpServlet {

    // Called once when the server starts — creates the DB table.
    @Override
    public void init() throws ServletException {
        Database.initialize();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            ItemDao dao = new ItemDao();
            req.setAttribute("counts", dao.countByStatus());
            req.setAttribute("recent", dao.findRecent(5));
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }
}
