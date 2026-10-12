package sherlock.web;

import sherlock.db.ItemDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles the items listing page (/items).
 * Supports text search and status filtering.
 */
public class ItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String query = req.getParameter("q");
            String status = req.getParameter("status");

            ItemDao dao = new ItemDao();
            req.setAttribute("items", dao.search(query, status));
            req.setAttribute("q", query);
            req.setAttribute("status", status);
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/items.jsp").forward(req, resp);
    }
}
