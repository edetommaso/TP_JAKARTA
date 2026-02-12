package com.example.tpjakarta.repositories;

import com.example.tpjakarta.utils.JPAUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.function.Function;

public abstract class BaseRepository<T> {

    protected final Class<T> entityClass;

    public BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T findById(Long id) {
        return executeInTransaction(entityManager -> entityManager.find(entityClass, id));
    }

    public List<T> findAll() {
        return executeInTransaction(entityManager -> entityManager.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList());
    }

    public void create(T entity) {
        executeInTransaction(entityManager -> {
            entityManager.persist(entity);
            return null;
        });
    }

    public T update(T entity) {
        return executeInTransaction(entityManager -> entityManager.merge(entity));
    }

    public void delete(Long id) {
        executeInTransaction(entityManager -> {
            T entity = entityManager.find(entityClass, id);
            if (entity != null) {
                entityManager.remove(entity);
            }
            return null;
        });
    }

    protected <R> R executeInTransaction(Function<EntityManager, R> function) {
        EntityManager entityManager = JPAUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            R result = function.apply(entityManager);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
