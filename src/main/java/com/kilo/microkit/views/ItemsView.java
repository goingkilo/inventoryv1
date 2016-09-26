package com.kilo.microkit.views;

import com.kilo.microkit.api.model.InventoryItem;
import io.dropwizard.views.View;

import java.util.List;

/**
 * Created by kraghunathan on 7/19/16.
 */
public class ItemsView extends View {

    private final List<InventoryItem> items;

    public ItemsView(List<InventoryItem> items) {
        super("items.ftl");
        this.items = items;
    }

    public List<InventoryItem> getItems() {

        return items;
    }
}