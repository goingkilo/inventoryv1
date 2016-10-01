package com.kilo.microkit.core.provider;


import com.kilo.microkit.api.parser.FlipkartParser;
import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;

import javax.ws.rs.client.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * FlipkartProvider provides following functionality
 * . a list of categories
 * . a list of products for a given category
 * . api to timecheck if both of above is not stale (ie less than 10 hrs old)
 * . search results for a given search term
 * TODO: offers and deals-of-the-day
 * TODO: strip out http calls (HTTPUtil) ,strip out db calls(FlipkartDBProvider, add FlipkartCache
 * <p>
 * Created by kraghunathan on
 * 9/8/16.
 */
public class FlipkartProvider {

    public static final String search = "https://affiliate-api.flipkart.net/affiliate/search/json?query=searchTerm&resultCount=Count";
    public static final String categoriesURL = "https://affiliate-api.flipkart.net/affiliate/api/goingkilo.json";

    /**
     * Search me
     *
     * @param client
     * @param searchTerm
     * @param count
     * @return
     * @throws SocketTimeoutException
     */
    public static List<Product> search(Client client, String searchTerm, int count) throws SocketTimeoutException {

        String s = search.replace("searchTerm", searchTerm).replace("=Count", "=" + count);
        return FlipkartParser.parseSearchResults(get(client, s));
    }

    /**
     * Get categories from the affiliate site.
     * If this information is not present as a field, then check
     * TODO:redis cache.
     * If unavailable, check the DB , and if not stale, get from
     * distant affiliate site as a last resort
     * TODO: This information expires in 10 hrs.needs isFresh() and refresh()
     *
     * @param client
     * @param categoryDAO
     * @return
     */
    public static List<Category> categories(Client client, CategoryDAO categoryDAO) {

        try {
            List<Category> categories= categoryDAO.getCategories();
            if (categories == null || categories.size() == 0) {
                String j = get(client, categoriesURL);
                categories = FlipkartParser.parseCategories(j);
                categoryDAO.saveMany(categories);
            }

            // sort map by value
            // Ordering<String> valueComparator = Ordering.natural().onResultOf(Functions.forMap(categories));
            // categories = ImmutableSortedMap.copyOf(categories, valueComparator);

            if (categories != null) {
                Collections.sort(categories, new Comparator<Category>() {
                    @Override
                    public int compare(Category c1, Category c2) {
                        return c1.getTitle().compareTo(c2.getTitle());
                    }
                });
                return categories;
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return new ArrayList<Category>();
    }

    /**
     * Get list of products for a given category title
     * by either from DB if already existing or
     * fetching from affiliate API using corresponding fetch URL
     * TODO: this info expires in 10 hrs,needs isFresh()  & refresh()
     *
     * @param categoryURL
     * @return
     */
    public static List<Product> products(Client client, ProductDAO dao, String category, String categoryURL, int offset, int size) {
        List<Product> products = new ArrayList<Product>();
        try {
            products = dao.getInventoryItems(category);
            if (products == null || products.size() == 0) {
                String json = get(client, categoryURL);
                products = FlipkartParser.parseProductInfo(json);
                dao.saveMany(products ,category);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        if( products.size() > ( offset + size) ) {
            return products.subList(offset, offset + size);
        }
        return products;
    }

    /**
     * what we actually call
     *
     * @param client
     * @param dao
     * @param categoryURL
     * @return
     */
    public static List<Product> products(Client client, ProductDAO dao, String category, String categoryURL) {
        return products(client, dao, category, categoryURL, 0, 30);
    }


    /**
     * Utility method to handle http get for my affiliate id
     *
     * @param client
     * @param query
     * @return
     * @throws SocketTimeoutException
     */
    public static String get(Client client, String query) throws SocketTimeoutException {
        String s = query;
        HttpURLConnection con = null;
        try {
            URL url = new URL(s);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Fk-Affiliate-Token", "1368e5baaf8e4bcdb442873d4aa8ef6e");
            con.setRequestProperty("Fk-Affiliate-Id", "goingkilo");

            int status = con.getResponseCode();


            if (status == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return "";
    }


    public static void main(String[] args) throws SocketTimeoutException {
        List<Category> a = categories(null, null);
        for (Category s : a) {
            System.out.println(s.getTitle());
            System.out.println(s.getUrl());
        }
        List<Product> b = products(null, null, null, "desktopsURL");

        for (Product x : b) {
            System.out.println(x);
        }
    }

}
