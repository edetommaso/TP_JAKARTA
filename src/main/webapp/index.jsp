<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Accueil</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body, html {
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #f8f9fa;
        }
        .content-container {
            text-align: center;
        }
    </style>
</head>
<body>

<div class="content-container">
    <h1 class="display-4 mb-3">Gerer vos annonces</h1>
    <p class="lead mb-4">Yo wagwan man</p>
    <div class="d-grid gap-2 col-6 mx-auto">
        <a href="annonce-add" class="btn btn-success btn-lg">Ajouter une annonce</a>
        <a href="annonce-list" class="btn btn-primary btn-lg">Voir toutes les annonces</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>