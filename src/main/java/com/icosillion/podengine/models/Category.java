package com.icosillion.podengine.models;

import java.util.HashSet;
import java.util.Set;

public class Category {

    private String category;

    private Set<Category> subcategories;

    public Category(String category) {
        this(category, new HashSet<Category>());
    }

    public Category(String category, Set<Category> subcategories) {
        this.category = category;
        this.subcategories = subcategories;
    }

    public String getName()
    {
        return this.category;
    }

    public Set<Category> getSubcategories() {
        return this.subcategories;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.category);

        if (!this.subcategories.isEmpty()) {
            for (Category subcategory : this.subcategories) {
                builder.append(" > ");
                builder.append(subcategory.toString());
            }
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Category)) {
            return false;
        }

        Category other = (Category) o;

        return this.toString().equals(other.toString());
    }
}
