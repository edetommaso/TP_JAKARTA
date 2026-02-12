<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Modifier une Annonce</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp" />
<div class="container mt-4">
    <h1>Modifier une annonce</h1>
    <form action="annonce-update" method="post" class="mt-3">
        <input type="hidden" name="id" value="${annonce.id}">

        <div class="mb-3">
            <label for="title" class="form-label">Titre :</label>
            <input type="text" class="form-control" id="title" name="title" value="${annonce.title}" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Description :</label>
            <textarea class="form-control" id="description" name="description" rows="3" required>${annonce.description}</textarea>
        </div>

        <div class="mb-3">
            <label for="adress" class="form-label">Adresse :</label>
            <input type="text" class="form-control" id="adress" name="adress" value="${annonce.adress}" required>
        </div>

        <div class="mb-3">
            <label for="mail" class="form-label">Email :</label>
            <input type="email" class="form-control" id="mail" name="mail" value="${annonce.mail}" required>
        </div>

        <div class="mb-3">
            <label for="category" class="form-label">Catégorie :</label>
            <select class="form-select" id="category" name="category">
                <option value="">-- Sélectionnez une catégorie --</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.id}" ${annonce.category.id == category.id ? 'selected' : ''}>${category.label}</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label for="status" class="form-label">Statut :</label>
            <select class="form-select" id="status" name="status" required>
                <c:forEach var="status" items="${statuses}">
                    <option value="${status}" ${annonce.status == status ? 'selected' : ''}>${status}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Mettre à jour</button>
        <a href="annonce-list" class="btn btn-danger me-2">Annuler</a>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
