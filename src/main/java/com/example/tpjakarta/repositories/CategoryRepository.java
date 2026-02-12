package com.example.tpjakarta.repositories;

import com.example.tpjakarta.beans.Category;

public class CategoryRepository extends BaseRepository<Category> {
    public CategoryRepository() {
        super(Category.class);
    }
}
