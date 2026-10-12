package sherlock.web;

import sherlock.db.ItemDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles the item handover process (/admin/handover).
 * Admin enters a 6-character token to mark matched items as RETURNED.
 */
public class HandoverServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/handover.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String token = req.getParameter("token");
            if (token == null || token.isBlank()) {
                req.setAttribute("error", "Please enter a token.");
                req.getRequestDispatcher("/handover.jsp").forward(req, resp);
                return;
            }

            ItemDao dao = new ItemDao();
            boolean success = dao.markReturned(token.trim().toUpperCase());

            if (success) {
                req.setAttribute("message", "Items marked as RETURNED successfully!");
            } else {
                req.setAttribute("error", "Invalid token, or items were already returned.");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/handover.jsp").forward(req, resp);
    }
}
