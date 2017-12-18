package com.icosillion.podengine.models;

import java.util.HashSet;
import java.util.Set;

public class Category {

    private String category;

    private String domain;

    private Set<Category> subcategories;

    public Category(String category) {
        this(category, null);
    }

    public Category(String category, String domain) {
        this(category, domain, new HashSet<Category>());
    }

    public Category(String category, String domain, Set<Category> subcategories) {
        this.category = category;
        this.domain = domain;
        this.subcategories = subcategories;
    }

    public String getName()
    {
        return this.category;
    }

    public String getDomain() {
        return this.domain;
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

        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + subcategories.hashCode();
        return result;
    }
}
