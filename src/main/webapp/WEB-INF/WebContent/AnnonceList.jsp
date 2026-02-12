<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Liste des Annonces</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp" />
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1>Liste des annonces</h1>
        <div>
            <a href="annonce-add" class="btn btn-success me-2">Ajouter une annonce</a>
        </div>
    </div>

    <!-- Search Form -->
    <form action="annonce-list" method="get" class="mb-4">
        <div class="row g-3">
            <div class="col-md-4">
                <input type="text" name="keyword" class="form-control" placeholder="Mot-clé..." value="${keyword}">
            </div>
            <div class="col-md-3">
                <select name="category" class="form-select">
                    <option value="">Toutes les catégories</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.id}" ${cat.id == categoryId ? 'selected' : ''}>${cat.label}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Rechercher</button>
            </div>
        </div>
    </form>

    <c:if test="${not empty annoncesFront}">
        <table class="table table-striped table-hover">
            <thead class="bg-light">
            <tr>
                <th scope="col">Titre</th>
                <th scope="col">Description</th>
                <th scope="col">Auteur</th>
                <th scope="col">Catégorie</th>
                <th scope="col">Date</th>
                <th scope="col">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="annonce" items="${annoncesFront}">
                <tr>
                    <td><c:out value="${annonce.title}"/></td>
                    <td><c:out value="${annonce.description}"/></td>
                    <td><c:out value="${annonce.author.username}"/></td>
                    <td><c:out value="${annonce.category.label}"/></td>
                    <td><fmt:formatDate value="${annonce.date}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td>
                        <c:if test="${sessionScope.user.id == annonce.author.id}">
                            <a href="annonce-update?id=${annonce.id}" class="btn btn-primary btn-sm">Modifier</a>
                            <a href="annonce-delete?id=${annonce.id}" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette annonce ?');">Supprimer</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- Pagination -->
        <nav aria-label="Page navigation">
            <ul class="pagination">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                        <a class="page-link" href="annonce-list?page=${i}&keyword=${keyword}&category=${categoryId}&status=${status}">${i}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>

    <c:if test="${empty annoncesFront}">
        <div class="col">
            <p class="text-center">Aucune annonce à afficher pour le moment.</p>
        </div>
    </c:if>

</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
