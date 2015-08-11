package com.acme.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class Product {

    private final Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<Category> categories;
	private String imageUrl;

    public Product(
		Long id,
		String name,
		String description,
		BigDecimal price,
		List<Category> categories,
		String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
		this.categories = categories;
		this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

	public List<Category> getCategories() {
		return categories != null ? categories : Collections.<Category>emptyList();
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
