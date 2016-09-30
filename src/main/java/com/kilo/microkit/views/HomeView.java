package com.kilo.microkit.views;

import com.kilo.microkit.api.model.ProductInfo;
import io.dropwizard.views.View;

import java.util.List;
import java.util.Map;

/**
 * Created by kraghunathan on 9/29/16.
 */
public class HomeView extends View {

    private   List<ProductInfo> products;

    private Map<String, String> categories;

    public HomeView(Map<String, String> categories, List<ProductInfo> products ) {
        super("home.ftl");
        this.categories = categories;
        this.products   = products;
    }

    public Map<String, String> getCategories() {
        return categories;
    }

    public List<ProductInfo> getProducts() {
        return products;
    }


}
