package com.kilo.microkit.core.provider;

import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.model.Category;

import java.util.List;

/**
 * Created by kraghunathan on 10/1/16.
 */
public class FlipkartCachedVersion {
    static CategoryDAO categoryDAO;


    public static List<Category> categories() {
        List<Category> categories = categoryDAO.getCategories();
        return categories;
    }
}
