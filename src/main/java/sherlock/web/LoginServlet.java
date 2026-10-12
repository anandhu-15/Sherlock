package sherlock.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles admin login (/admin/login).
 * Uses one hardcoded password stored in a constant.
 */
public class LoginServlet extends HttpServlet {

    // ── The single admin password. Change this as needed. ──
    private static final String ADMIN_PASSWORD = "sherlock2024";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String password = req.getParameter("password");
        if (ADMIN_PASSWORD.equals(password)) {
            // Store login flag in session and redirect to handover page.
            req.getSession().setAttribute("admin", true);
            resp.sendRedirect(req.getContextPath() + "/admin/handover");
        } else {
            req.setAttribute("error", "Wrong password. Try again.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
