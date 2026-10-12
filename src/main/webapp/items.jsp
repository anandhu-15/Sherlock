<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Items - Sherlock</title>
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
    <h1>All Items</h1>

    <!-- ── Search and filter form ── -->
    <form method="get" action="${pageContext.request.contextPath}/items" class="search-bar">
        <input type="text" name="q" placeholder="Search items..."
               value="<c:out value='${q}' />">
        <select name="status">
            <option value="">All statuses</option>
            <option value="LOST"     ${status eq 'LOST'     ? 'selected' : ''}>LOST</option>
            <option value="FOUND"    ${status eq 'FOUND'    ? 'selected' : ''}>FOUND</option>
            <option value="MATCHED"  ${status eq 'MATCHED'  ? 'selected' : ''}>MATCHED</option>
            <option value="RETURNED" ${status eq 'RETURNED' ? 'selected' : ''}>RETURNED</option>
        </select>
        <button type="submit" class="btn-primary">Search</button>
    </form>

    <c:choose>
        <c:when test="${empty items}">
            <p>No items found.</p>
        </c:when>
        <c:otherwise>
            <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>Type</th>
                        <th>Item</th>
                        <th>Description</th>
                        <th>Location</th>
                        <th>Reporter</th>
                        <th>Contact</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Map</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${items}">
                        <tr>
                            <td><c:out value="${item.type}" /></td>
                            <td><c:out value="${item.name}" /></td>
                            <td><c:out value="${item.description}" /></td>
                            <td><c:out value="${item.location}" /></td>
                            <td><c:out value="${item.reporter}" /></td>
                            <td><c:out value="${item.contact}" /></td>
                            <td><span class="status-${fn:toLowerCase(item.status)}">
                                <c:out value="${item.status}" /></span></td>
                            <td><c:out value="${item.createdAt}" /></td>
                            <td>
                                <c:if test="${item.latitude != null and item.longitude != null}">
                                    <a href="https://www.google.com/maps?q=${item.latitude},${item.longitude}"
                                       target="_blank">Map</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            </div>
        </c:otherwise>
    </c:choose>
</main>

</body>
</html>
