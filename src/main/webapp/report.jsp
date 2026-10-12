<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Report Item - Sherlock</title>
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
    <h1>Report a Lost or Found Item</h1>

    <c:if test="${not empty error}">
        <p class="error-msg"><c:out value="${error}" /></p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/report" class="form-card">

        <label for="type">Type</label>
        <select id="type" name="type" required>
            <option value="LOST">LOST &mdash; I lost something</option>
            <option value="FOUND">FOUND &mdash; I found something</option>
        </select>

        <label for="name">Item Name</label>
        <input type="text" id="name" name="name" placeholder="e.g. Blue Water Bottle" required>

        <label for="description">Description</label>
        <textarea id="description" name="description" rows="3"
                  placeholder="Describe the item in detail..." required></textarea>

        <label for="location">Campus Location</label>
        <select id="location" name="location" required>
            <option value="">-- Select location --</option>
            <option>Main Block</option>
            <option>New Block</option>
            <option>Alumni Block</option>
            <option>Library</option>
            <option>Canteen</option>
            <option>CSE Workshop</option>
            <option>Mech Workshop</option>
            <option>Civil Workshop</option>
            <option>Auditorium</option>
            <option>Men's Hostel</option>
            <option>Ladies Hostel</option>
        </select>

        <label for="reporter">Your Name</label>
        <input type="text" id="reporter" name="reporter" required>

        <label for="contact">Contact Number</label>
        <input type="text" id="contact" name="contact" placeholder="e.g. 9876543210" required>

        <!-- Hidden fields for GPS coordinates -->
        <input type="hidden" id="latitude" name="latitude">
        <input type="hidden" id="longitude" name="longitude">

        <div class="btn-row">
            <button type="button" onclick="getLocation()" class="btn-secondary">
                &#128205; Use my location
            </button>
            <span id="loc-status"></span>
        </div>

        <button type="submit" class="btn-primary">Submit Report</button>
    </form>
</main>

<!-- ~10 lines of plain JavaScript for geolocation -->
<script>
function getLocation() {
    if (!navigator.geolocation) {
        document.getElementById('loc-status').textContent = 'Geolocation not supported.';
        return;
    }
    document.getElementById('loc-status').textContent = 'Getting location...';
    navigator.geolocation.getCurrentPosition(
        function(pos) {
            document.getElementById('latitude').value  = pos.coords.latitude.toFixed(6);
            document.getElementById('longitude').value = pos.coords.longitude.toFixed(6);
            document.getElementById('loc-status').textContent = 'Location captured!';
        },
        function() {
            document.getElementById('loc-status').textContent = 'Location access denied.';
        }
    );
}
</script>

</body>
</html>
