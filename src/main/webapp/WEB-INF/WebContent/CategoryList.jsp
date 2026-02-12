<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Liste des Catégories</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp" />
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1>Liste des catégories</h1>
        <div>
            <a href="category-add" class="btn btn-success me-2">Ajouter une catégorie</a>
        </div>
    </div>

    <c:if test="${not empty categories}">
        <table class="table table-striped table-hover">
            <thead class="bg-light">
            <tr>
                <th scope="col">Label</th>
                <th scope="col">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="category" items="${categories}">
                <tr>
                    <td><c:out value="${category.label}"/></td>
                    <td>
                        <a href="category-update?id=${category.id}" class="btn btn-primary btn-sm">Modifier</a>
                        <a href="category-delete?id=${category.id}" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette catégorie ?');">Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty categories}">
        <div class="col">
            <p class="text-center">Aucune catégorie à afficher pour le moment.</p>
        </div>
    </c:if>

</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
