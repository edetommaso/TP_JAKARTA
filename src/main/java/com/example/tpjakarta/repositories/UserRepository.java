package com.example.tpjakarta.repositories;

import com.example.tpjakarta.beans.User;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UserRepository extends BaseRepository<User> {
    public UserRepository() {
        super(User.class);
    }

    public User findByUsername(String username) {
        return executeInTransaction(entityManager -> {
            try {
                TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
                query.setParameter("username", username);
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    public User findByEmail(String email) {
        return executeInTransaction(entityManager -> {
            try {
                TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
                query.setParameter("email", email);
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }
}
