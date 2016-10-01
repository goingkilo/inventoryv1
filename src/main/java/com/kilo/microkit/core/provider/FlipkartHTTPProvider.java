package com.kilo.microkit.core.provider;


import com.kilo.microkit.api.parser.FlipkartParser;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FlipkartProvider provides following functionality
 * . a list of categories
 * . a list of products for a given category
 * . api to timecheck if both of above is not stale (ie less than 10 hrs old)
 * . search results for a given search term
 * TODO: offers and deals-of-the-day
 * <p>
 * Created by kraghunathan on
 * 9/8/16.
 */
public class FlipkartHTTPProvider {

    public static final String search = "https://affiliate-api.flipkart.net/affiliate/search/json?query=searchTerm&resultCount=Count";
    public static final String categoriesURL = "https://affiliate-api.flipkart.net/affiliate/api/goingkilo.json";

    /**
     * Search me
     *
     * @param searchTerm
     * @param count
     * @return
     * @throws SocketTimeoutException
     */
    public List<Product> search(String searchTerm, int count) throws SocketTimeoutException {

        String s = search.replace("searchTerm", searchTerm).replace("=Count", "=" + count);
        return FlipkartParser.parseSearchResults(get( s));
    }

    /**
     * Get categories from the affiliate site.
     * If this information is not present as a field, then check
     * TODO:redis cache.
     * If unavailable, check the DB , and if not stale, get from
     * distant affiliate site as a last resort
     * TODO: This information expires in 10 hrs.needs isFresh() and refresh()
     *
     */
    public List<Category> categories() {

        List<Category> categories = null;
        String j = get(categoriesURL);
        categories = FlipkartParser.parseCategories(j);
        return (categories == null) ? new ArrayList<Category>() : categories;

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
    public List<Product> products(String categoryURL, int offset, int size) {
        List<Product> products = new ArrayList<Product>();
        String json = get(categoryURL);
        products = FlipkartParser.parseProductInfo(json);
        return products.subList(offset, offset + size);
    }

    /**
     * what we actually call
     *
     * @param categoryURL
     * @return
     */
    public  List<Product> products(String categoryURL) {
        return products(categoryURL, 0, 15);
    }


    /**
     * Utility method to handle http get for my affiliate id
     *
     * @param query
     * @return
     */
    public String get(String query) {
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
                con = null;
            }

        }
        return "";
    }


    public static void main(String[] args) {
        FlipkartHTTPProvider f = new FlipkartHTTPProvider();
        List<Category> a = f.categories();
        String url = a.get(0).getUrl();
        System.out.println(url);
        List<Product> b = f.products(url );

        for (Product x : b) {
            System.out.println(x);
        }
    }

}
