package com.kilo.microkit.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by kraghunathan on 9/17/16.
 */
@Entity
@Table(name = "Product")
public class Product {

    private String id;
    private String category;
    private String title;
    private String desc;
    private String image;
    private String price;
    private String color;
    private String brand;
    private String url;
    private String inStock;
    private String available;

    public Product() {

    }

    @Column(name = "in_stock")
    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    @Id
    @Column(name = "a_id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "title",columnDefinition="TEXT")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description",columnDefinition="TEXT")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "price")
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Column(name = "color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name="brand")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Column(name="is_available")
    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public Product(String id, String category, String title, String desc, String image, String price, String color, String brand, String url, String inStock, String available) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.price = price;
        this.color = color;
        this.brand = brand;
        this.url = url;
        this.inStock = inStock;
        this.available = available;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", image='" + image + '\'' +
                ", price='" + price + '\'' +
                ", color='" + color + '\'' +
                ", brand='" + brand + '\'' +
                ", url='" + url + '\'' +
                ", inStock='" + inStock + '\'' +
                ", available='" + available + '\'' +
                '}';
    }
}

