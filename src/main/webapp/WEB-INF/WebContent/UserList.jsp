<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Liste des Utilisateurs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp" />
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1>Liste des utilisateurs</h1>
        <div>
            <a href="user-add" class="btn btn-success me-2">Ajouter un utilisateur</a>
        </div>
    </div>

    <c:if test="${not empty users}">
        <table class="table table-striped table-hover">
            <thead class="bg-light">
            <tr>
                <th scope="col">Username</th>
                <th scope="col">Email</th>
                <th scope="col">Date de création</th>
                <th scope="col">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td><c:out value="${user.username}"/></td>
                    <td><c:out value="${user.email}"/></td>
                    <td><fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td>
                        <a href="user-update?id=${user.id}" class="btn btn-primary btn-sm">Modifier</a>
                        <a href="user-delete?id=${user.id}" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?');">Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty users}">
        <div class="col">
            <p class="text-center">Aucun utilisateur à afficher pour le moment.</p>
        </div>
    </c:if>

</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
