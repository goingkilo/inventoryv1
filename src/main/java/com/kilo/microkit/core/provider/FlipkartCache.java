package com.kilo.microkit.core.provider;


import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;

import javax.ws.rs.client.Client;
import java.net.SocketTimeoutException;
import java.util.List;

public class FlipkartCache {

    public static List<Product> search(Client client, ProductDAO productsDAO, String searchTerm, int count) throws SocketTimeoutException {
        return null;
    }

    public static List<Category> categories(Client client, CategoryDAO categoryDAO) {
        return null;
    }

    public static List<Product> products(Client client, ProductDAO dao, String category, String categoryURL, int offset, int size) {
        return null;
    }

    public static List<Product> products(Client client, ProductDAO dao, String category, String categoryURL) {
        return null;
    }


}
