<%@ page import="com.example.tpjakarta.beans.Annonce" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Liste des Annonces</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1>Liste des annonces</h1>
        <div>
            <a href="annonce-add" class="btn btn-success me-2">Ajouter une annonce</a>
            <a href="index.jsp" class="btn btn-info">Accueil</a>
        </div>
    </div>

    <%
        List<Annonce> annonces = (List<Annonce>) request.getAttribute("annonces-front");
        if (annonces != null && !annonces.isEmpty()) {
    %>
    <table class="table table-striped table-hover">
        <thead class="bg-light">
        <tr>
            <th scope="col">Titre</th>
            <th scope="col">Description</th>
            <th scope="col">Adresse</th>
            <th scope="col">Email</th>
            <th scope="col">Date</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Annonce annonce : annonces) {
        %>
        <tr>
            <td><%= annonce.getTitle() %></td>
            <td><%= annonce.getDescription() %></td>
            <td><%= annonce.getAdress() %></td>
            <td><%= annonce.getMail() %></td>
            <td><%= annonce.getDate() %></td>
            <td>
                <a href="annonce-update?id=<%= annonce.getId() %>" class="btn btn-primary btn-sm">Modifier</a>
                <a href="annonce-delete?id=<%= annonce.getId() %>" class="btn btn-danger btn-sm">Supprimer</a>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
    } else {
    %>
    <div class="col">
        <p class="text-center">Aucune annonce à afficher pour le moment.</p>
    </div>
    <%
        }
    %>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
