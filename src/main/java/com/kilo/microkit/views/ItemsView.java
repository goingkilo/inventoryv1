package com.kilo.microkit.views;

import com.kilo.microkit.db.model.Product;
import io.dropwizard.views.View;

import java.util.List;

/**
 * Created by kraghunathan on 7/19/16.
 */
public class ItemsView extends View {

    private final List<Product> items;

    public ItemsView(List<Product> items) {
        super("items.ftl");
        this.items = items;
    }

    public List<Product> getItems() {

        return items;
    }
}