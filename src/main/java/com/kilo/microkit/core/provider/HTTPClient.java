package com.kilo.microkit.core.provider;

import com.kilo.microkit.api.parser.FlipkartParser;
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
 */
public class HTTPClient {

    public static final String search = "https://affiliate-api.flipkart.net/affiliate/search/json?query=searchTerm&resultCount=Count";
    public static final String categoriesURL = "https://affiliate-api.flipkart.net/affiliate/api/goingkilo.json";

    public static boolean local = false;

    private Client client;

    public HTTPClient() {
    }

    public HTTPClient(Client client) {
        this.client = client;
    }

    /**
     * Flipkart returns only 10 results at a time
     * @param searchTerm
     * @return
     * @throws SocketTimeoutException
     */
    public List<Product> search(String searchTerm, int count) throws SocketTimeoutException {

        String url = search.replace("searchTerm", searchTerm).replace("=Count", "=" + count);
        String json = httpGet( url);
        List<Product> remoteSearchResults = FlipkartParser.parseSearchResults(json);
        return remoteSearchResults;
    }

    public List<Category> categories() {
        try {
            String json = httpGet(categoriesURL);
            List<Category> categories = FlipkartParser.parseCategories(json);
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

    public List<Product> products( Category category) {
        String categoryString  = category.getTitle();
        String categoryURL = category.getUrl();

        List<Product> products = new ArrayList<Product>();
        try {
            String json = httpGet(categoryURL);
            products = FlipkartParser.parseProductInfo(json);

            Collections.sort(products, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return Float.valueOf(p1.getPrice()).compareTo(Float.valueOf(p2.getPrice()));
                }
            });

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return products;
    }

//    private boolean saveLocal(String name, String json) {
//        File file = new File( name + ".json");
//        try {
//            Files.write(json, file, Charsets.UTF_8);
//            System.out.println( "saved " + name + " to disk.");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }

    private String httpGet(String query) throws SocketTimeoutException {
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

}
