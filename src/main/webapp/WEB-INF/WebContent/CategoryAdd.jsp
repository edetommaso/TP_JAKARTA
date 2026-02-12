<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ajouter une Catégorie</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp" />
<jsp:include page="navbar.jsp" />
<div class="container mt-4">
    <h1>Ajouter une nouvelle catégorie</h1>
    <form action="category-add" method="post" class="mt-3">
        <div class="mb-3">
            <label for="label" class="form-label">Label :</label>
            <input type="text" class="form-control" id="label" name="label" required>
        </div>
        <button type="submit" class="btn btn-primary">Ajouter</button>
        <a href="category-list" class="btn btn-secondary">Annuler</a>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
