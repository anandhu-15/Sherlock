<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Item Handover - Sherlock</title>
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
    <h1>Item Handover</h1>
    <p>Enter the 6-character handover token to mark matched items as returned.</p>

    <c:if test="${not empty error}">
        <p class="error-msg"><c:out value="${error}" /></p>
    </c:if>
    <c:if test="${not empty message}">
        <p class="success-msg"><c:out value="${message}" /></p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/admin/handover" class="form-card">
        <label for="token">Handover Token</label>
        <input type="text" id="token" name="token" maxlength="6"
               placeholder="e.g. AB3X9K" required
               style="text-transform:uppercase; letter-spacing:0.3em; font-size:1.3rem;">
        <button type="submit" class="btn-primary">Confirm Handover</button>
    </form>
</main>

</body>
</html>
