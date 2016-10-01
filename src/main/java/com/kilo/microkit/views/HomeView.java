package com.kilo.microkit.views;

import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;
import io.dropwizard.views.View;

import java.util.List;

/**
 * Created by kraghunathan on 9/29/16.
 */
public class HomeView extends View {

    private   List<Product> products;
    private List<Category> categories;

    public HomeView(List<Category> categories, List<Product> products ) {
        super("home.ftl");
        this.categories = categories;
        this.products   = products;
    }

    public List<Category> getCategories() {

        return categories;
    }

    public List<Product> getProducts() {
        return products;
    }


}
