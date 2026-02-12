package com.example.tpjakarta.repositories;

import com.example.tpjakarta.beans.Annonce;
import com.example.tpjakarta.beans.Category;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.utils.AnnonceStatus;
import com.example.tpjakarta.utils.JPAUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnonceRepository extends BaseRepository<Annonce> {
    public AnnonceRepository() {
        super(Annonce.class);
    }

    public List<Annonce> search(String keyword, Category category, AnnonceStatus status, User currentUser, int page, int pageSize) {
        return executeInTransaction(entityManager -> {
            StringBuilder jpql = new StringBuilder("SELECT a FROM Annonce a WHERE 1=1");
            Map<String, Object> parameters = new HashMap<>();

            if (currentUser != null) {
                jpql.append(" AND (a.status = :publishedStatus OR a.author = :currentUser)");
                parameters.put("publishedStatus", AnnonceStatus.PUBLISHED);
                parameters.put("currentUser", currentUser);
            } else {
                jpql.append(" AND a.status = :publishedStatus");
                parameters.put("publishedStatus", AnnonceStatus.PUBLISHED);
            }

            if (keyword != null && !keyword.isEmpty()) {
                jpql.append(" AND (a.title LIKE :keyword OR a.description LIKE :keyword)");
                parameters.put("keyword", "%" + keyword + "%");
            }

            if (category != null) {
                jpql.append(" AND a.category = :category");
                parameters.put("category", category);
            }

            if (status != null && currentUser != null) {
                // Status filter only applies for the current user viewing their own ads
                jpql.append(" AND a.status = :status");
                parameters.put("status", status);
            }

            TypedQuery<Annonce> query = entityManager.createQuery(jpql.toString(), Annonce.class);
            parameters.forEach(query::setParameter);

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            return query.getResultList();
        });
    }

    public Long countSearch(String keyword, Category category, AnnonceStatus status, User currentUser) {
        return executeInTransaction(entityManager -> {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(a) FROM Annonce a WHERE 1=1");
            Map<String, Object> parameters = new HashMap<>();

            if (currentUser != null) {
                jpql.append(" AND (a.status = :publishedStatus OR a.author = :currentUser)");
                parameters.put("publishedStatus", AnnonceStatus.PUBLISHED);
                parameters.put("currentUser", currentUser);
            } else {
                jpql.append(" AND a.status = :publishedStatus");
                parameters.put("publishedStatus", AnnonceStatus.PUBLISHED);
            }

            if (keyword != null && !keyword.isEmpty()) {
                jpql.append(" AND (a.title LIKE :keyword OR a.description LIKE :keyword)");
                parameters.put("keyword", "%" + keyword + "%");
            }

            if (category != null) {
                jpql.append(" AND a.category = :category");
                parameters.put("category", category);
            }

            if (status != null && currentUser != null) {
                jpql.append(" AND a.status = :status");
                parameters.put("status", status);
            }

            TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
            parameters.forEach(query::setParameter);

            return query.getSingleResult();
        });
    }
}
