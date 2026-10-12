<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Report Submitted - Sherlock</title>
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
    <h1>&#10004; Report Submitted!</h1>
    <p>Your <strong><c:out value="${item.type}" /></strong> report for
       &ldquo;<c:out value="${item.name}" />&rdquo; has been saved.</p>

    <%-- If a match is found, show the token clearly to both LOST and FOUND --%>
    <c:if test="${not empty token}">
        <div class="match-box">
            <h2>&#127881; Match Found!</h2>
            <p>Your report has matched with an existing <c:out value="${item.type eq 'LOST' ? 'FOUND' : 'LOST'}" /> item!
               Use this 6-character token to complete the handover at the admin office:</p>
            <p class="token"><c:out value="${token}" /></p>
            <p><strong>Note down this token!</strong> The admin verifies this token on the Handover page to mark the item as returned.</p>
        </div>
    </c:if>

    <c:if test="${empty token}">
        <p>No matching item found yet. We&rsquo;ll keep looking! Check the
           <a href="${pageContext.request.contextPath}/items">Items page</a>
           for updates.</p>
    </c:if>

    <p style="margin-top:1.5rem;">
        <a href="${pageContext.request.contextPath}/report" class="btn-primary">Report Another Item</a>
        <a href="${pageContext.request.contextPath}/home" class="btn-secondary">Back to Home</a>
    </p>
</main>

</body>
</html>
