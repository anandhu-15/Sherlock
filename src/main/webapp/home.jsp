<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sherlock - Campus Lost &amp; Found</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav>
    <div class="container nav-inner">
        <a href="${pageContext.request.contextPath}/home" class="logo">&#128269; Sherlock</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/report">Report</a>
            <a href="${pageContext.request.contextPath}/items">Items</a>
            <a href="${pageContext.request.contextPath}/admin/handover">Admin</a>
        </div>
    </div>
</nav>

<main class="container">
    <h1>Campus Lost &amp; Found</h1>
    <p class="subtitle">NSS College of Engineering, Palakkad</p>
    <p>Lost something on campus? Found an item that isn't yours? Report it here
       and our auto-matching system will try to connect lost items with found ones.</p>

    <!-- ── Status counts ── -->
    <div class="stats">
        <div class="stat-card">
            <span class="stat-number">${counts.getOrDefault('LOST', 0)}</span>
            <span class="status-lost">LOST</span>
        </div>
        <div class="stat-card">
            <span class="stat-number">${counts.getOrDefault('FOUND', 0)}</span>
            <span class="status-found">FOUND</span>
        </div>
        <div class="stat-card">
            <span class="stat-number">${counts.getOrDefault('MATCHED', 0)}</span>
            <span class="status-matched">MATCHED</span>
        </div>
        <div class="stat-card">
            <span class="stat-number">${counts.getOrDefault('RETURNED', 0)}</span>
            <span class="status-returned">RETURNED</span>
        </div>
    </div>

    <!-- ── 5 most recent reports ── -->
    <h2>Recent Reports</h2>
    <c:choose>
        <c:when test="${empty recent}">
            <p>No items reported yet.
               <a href="${pageContext.request.contextPath}/report">Be the first!</a></p>
        </c:when>
        <c:otherwise>
            <div class="table-wrap">
            <table>
                <thead>
                    <tr><th>Type</th><th>Item</th><th>Location</th><th>Status</th><th>Date</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${recent}">
                        <tr>
                            <td><c:out value="${item.type}" /></td>
                            <td><c:out value="${item.name}" /></td>
                            <td><c:out value="${item.location}" /></td>
                            <td><span class="status-${fn:toLowerCase(item.status)}">
                                <c:out value="${item.status}" /></span></td>
                            <td><c:out value="${item.createdAt}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            </div>
        </c:otherwise>
    </c:choose>

    <p style="margin-top:1.5rem;">
        <a href="${pageContext.request.contextPath}/items" class="btn-primary">View All Items</a>
        <a href="${pageContext.request.contextPath}/report" class="btn-primary">Report an Item</a>
    </p>
</main>

</body>
</html>
