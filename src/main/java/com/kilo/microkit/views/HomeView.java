package com.kilo.microkit.views;

import com.kilo.microkit.api.ProductInfo;
import io.dropwizard.views.View;

import java.util.List;
import java.util.Map;

/**
 * Created by kraghunathan on 9/29/16.
 */
public class HomeView extends View {

    private   List<ProductInfo> products;

    private   List<ProductInfo> products2;
    private Map<String, String> categories;

    public HomeView(Map<String, String> categories, List<ProductInfo> products ) {
        super("home.ftl");
        this.categories = categories;
        this.products   = products.subList(0, products.size()/2);
        this.products2  = products.subList( products.size()/2, products.size());
    }

    public Map<String, String> getCategories() {
        return categories;
    }

    public List<ProductInfo> getProducts() {
        return products;
    }

    public List<ProductInfo> getProducts2() {
        return products2;
    }



}
