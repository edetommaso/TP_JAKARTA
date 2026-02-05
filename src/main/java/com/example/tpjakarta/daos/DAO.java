package com.example.tpjakarta.daos;

import java.sql.Connection;
import java.util.List;

public abstract class DAO<T> {
    protected Connection con = null;

    public DAO(Connection con) {
        this.con = con;
    }

    public abstract boolean create(T obj);

    public abstract T findById(Long id);

    public abstract List<T> findAll();

    public abstract boolean update(T obj);

    public abstract boolean delete(Long id);
}
