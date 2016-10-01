package com.kilo.microkit.api.sample;


import javax.persistence.*;

@Entity
@Table(name = "ProductInfo")
public class ProductInfo {


    private String id;
    private String title;
    private String description;
    private Double mrp;
    private Double sellingPrice;
    private String productUrl;
    private String imageURL;
    private boolean inStock;

    public ProductInfo(){}

    @Column(name = "in_stock")
    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    @Id
    @Column(name = "p_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "title",columnDefinition="TEXT")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description",columnDefinition="TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "url")
    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    @Column(name = "price")
    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    @Override
    public String toString() {
        return "ProductInfo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", mrp=" + mrp +
                ", sellingPrice=" + sellingPrice +
                ", productUrl='" + productUrl + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", inStock=" + inStock +
                '}';
    }
}
