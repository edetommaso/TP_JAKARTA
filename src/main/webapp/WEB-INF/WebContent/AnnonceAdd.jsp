<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ajouter une Annonce</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-4">
    <h1>Ajouter une nouvelle annonce</h1>
    <form action="annonce-add" method="post" class="mt-3">
        <div class="mb-3">
            <label for="title" class="form-label">Titre :</label>
            <input type="text" class="form-control" id="title" name="title" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Description :</label>
            <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
        </div>

        <div class="mb-3">
            <label for="adress" class="form-label">Adresse :</label>
            <input type="text" class="form-control" id="adress" name="adress" required>
        </div>

        <div class="mb-3">
            <label for="mail" class="form-label">Email :</label>
            <input type="email" class="form-control" id="mail" name="mail" required>
        </div>

        <button type="submit" class="btn btn-primary">Envoyer</button>
        <a href="annonce-list" class="btn btn-danger me-2">Annuler</a>
        <a href="index.jsp" class="btn btn-info">Accueil</a>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
