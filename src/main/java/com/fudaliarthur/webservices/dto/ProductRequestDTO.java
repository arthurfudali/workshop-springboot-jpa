package com.fudaliarthur.webservices.dto;

import com.fudaliarthur.webservices.entities.Category;

import java.util.List;

public class ProductRequestDTO {
    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private List<Category> categories;

    private ProductRequestDTO() {
    }

    public ProductRequestDTO(String name, String description, Double price, String imgUrl, List<Category> categories) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.categories = categories;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}