package sherlock.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Protects all /admin/* pages except /admin/login.
 * Redirects unauthenticated users to the login page.
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Always allow access to the login page itself.
        if (req.getRequestURI().endsWith("/login")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if the user has logged in (session contains "admin" flag).
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("admin") != null) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/login");
        }
    }

    @Override
    public void destroy() {}
}
