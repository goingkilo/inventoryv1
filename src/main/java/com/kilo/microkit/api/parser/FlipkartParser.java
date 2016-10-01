package com.kilo.microkit.api.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kraghunathan on 9/30/16.
 * This class parses 3 types of JSON responses in 2 type of object
 * Search results, Product information, category information
 * as product, product and category objects
 */
public class FlipkartParser {

    /**
     * Parse search results into Product objects
     *
     * @param json
     * @return
     */
    public static List<Product> parseSearchResults(String json) {
        List<Product> ret = new ArrayList<Product>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readValue(json.getBytes(), JsonNode.class);
            for (int i = 0; i < rootNode.elements().next().size(); i++) {
                JsonNode item = rootNode.elements().next().get(i);
                Product p = new Product();

                p.setId(item.findValue("productId").asText());
                p.setCategory(item.findValue("categoryPaths").findValue("title").asText());
                p.setTitle(item.findValue("productAttributes").findValue("title").asText());
                p.setDesc(item.findValue("productDescription").asText());
                p.setPrice(item.findValue("sellingPrice").findValue("amount").asText());
                p.setUrl(item.findValue("productUrl").asText());
                p.setColor(item.findValue("color").asText());
                p.setInStock(item.findValue("inStock").asText());

                String[] imgkeys = new String[]{"200x200", "200x200", "200x200", "200x200", "default", "unknown"};
                for (String key : imgkeys) {
                    JsonNode img = item.findValue(key);
                    if (img != null) {
                        p.setImage(img.asText());
                    }
                }
                ret.add(p);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(json);
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Parse categories response to corresponding objects
     *
     * @param jsonData
     * @return
     */
    public static List<Category> parseCategories(String jsonData) {
        List<Category> l = new ArrayList<Category>();

        try {

            JSONObject obj = new JSONObject(jsonData);
            JSONObject listing = obj.getJSONObject("apiGroups").getJSONObject("affiliate").getJSONObject("apiListings");
            Iterator keys = listing.keys();
            while (keys.hasNext()) {

                String category_name = (String) keys.next();
                JSONObject variants = listing.getJSONObject(category_name).getJSONObject("availableVariants");

                // Sort the variants and get the latest version
                Iterator v_iterator = variants.keys();
                List<String> variant_keys = new ArrayList<String>();
                while (v_iterator.hasNext()) {
                    variant_keys.add((String) v_iterator.next());
                }
                Collections.sort(variant_keys);

                String category_url = variants.getJSONObject(variant_keys.get(0)).getString("get");


                l.add(new Category(category_name, category_url));
            }
        } catch (JSONException je) {
            je.printStackTrace();
            return null;
        }
        return l;
    }

    /**
     * Parse product json into ProductInfo objects
     * TODO: handle "nextURL" and multiple fetches.
     *
     * @param jsonData
     * @return
     */
    public static List<Product> parseProductInfo(String jsonData) {

        List<Product> items = new ArrayList<Product>();
        try {

            JSONObject obj = new JSONObject(jsonData);
            JSONArray productArray = obj.getJSONArray("productInfoList");

            for (int i = 0; i < productArray.length(); i++) {

                Product product = new Product();
                JSONObject inner_obj = productArray.getJSONObject(i).getJSONObject("productBaseInfo");
                product.setId(inner_obj.getJSONObject("productIdentifier").getString("productId"));

                JSONObject attributes = inner_obj.getJSONObject("productAttributes");
                product.setTitle(attributes.getString("title"));

                JSONObject images = attributes.getJSONObject("imageUrls");
                if (images.keySet().size() > 0) {
                    product.setImage((String) images.get(images.keySet().iterator().next()));
                }

//                product.setCategory(attributes.optString("category", ""));
                product.setDesc(attributes.optString("productDescription", ""));
                product.setPrice(String.valueOf(attributes.getJSONObject("sellingPrice").getDouble("amount")));
                product.setUrl(attributes.getString("productUrl"));
                product.setInStock( String.valueOf(attributes.getBoolean("inStock")));

                items.add(product);
            }
            return items;
        } catch (JSONException je) {
            return null;
        }

    }
}
