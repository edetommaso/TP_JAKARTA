<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="index.jsp">Master Annonce</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="annonce-list">Annonces</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="category-list">Catégories</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="user-list">Utilisateurs</a>
                </li>
            </ul>
            <c:if test="${not empty sessionScope.user}">
                <span class="navbar-text me-3">
                    Bonjour, <c:out value="${sessionScope.user.username}"/>
                </span>
                <a href="logout" class="btn btn-outline-light">Déconnexion</a>
            </c:if>
        </div>
    </div>
</nav>
