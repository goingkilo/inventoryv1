package com.kilo.microkit.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;

/**
 * Created by kraghunathan on 9/17/16.
 */
@Entity
@Table(name = "InventoryItems")
public class InventoryItem {

    private String id;
    private String category;
    private String title;
    private String desc;
    private String[] images;
    private String price;
    private String color;
    private String url;
    private String inStock;

    public InventoryItem() {

    }

    public InventoryItem(String id, String category, String title, String desc, String[] images, String price, String color, String url, String inStock) {

        this.id = id;
        this.category = category;
        this.title = title;
        this.desc = desc;
        this.images = images;
        this.price = price;
        this.color = color;
        this.url = url;
        this.inStock = inStock;

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

//    @Column(name = "images")
    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
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

    @Override
    public String toString() {
        return "Product{" +
                "\nid='" + id + '\'' +
                ",\ncategory='" + category + '\'' +
                ",\ntitle='" + title + '\'' +
                ",\ndesc='" + desc + '\'' +
                ",\nimages=" + Arrays.toString(images) +
                ",\nprice='" + price + '\'' +
                ",\ncolor='" + color + '\'' +
                ",\nurl='" + url + '\'' +
                ",\ninStock='" + inStock + '\'' +
                "\n}";
    }
}
