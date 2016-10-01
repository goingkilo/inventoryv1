package com.kilo.microkit.api.sample; /***
 * The main class to execute.
 * Please refer to the instructions.txt
 *
 *  author vijay.v flipkart.com
 *  version 1.0
 * Copyright (c) Flipkart India Pvt. Ltd.
 */

import java.util.*;

public class APIFeeds {

    private DataParser parser;

    public APIFeeds(String affiliateId, String affiliateToken, String downloadType) {
        parser = new JSONDataParser(affiliateId, affiliateToken);
        try {
            parser.initializeProductDirectory();
        } catch (AffiliateAPIException e) {
            e.printStackTrace();
        }
    }

    public DataParser getParser() {
        return parser;
    }


    public Map<String, String> categories() throws AffiliateAPIException {
        return parser.getProductDirectory() ;
    }

    public List<ProductInfo> products( String category) throws AffiliateAPIException {
        List<ProductInfo> ret = new ArrayList<ProductInfo>();

        Iterator<ProductInfo> products_iterator = parser.getProductList(category).listIterator();
        while (products_iterator.hasNext()) {
            ProductInfo product = products_iterator.next();
            if (product.isInStock()) {
                ret.add(product);
            }
        }
        return ret;
    }

    public static void main(String args[]) throws AffiliateAPIException {


        APIFeeds feeds = new APIFeeds("goingkilo", "1368e5baaf8e4bcdb442873d4aa8ef6e", "no");
        try {
            Map<String, String> cats = feeds.categories();
            List<ProductInfo> ret = feeds.products("grooming_beauty_wellness");
            System.out.println(ret);

        } catch (AffiliateAPIException e) {
            e.printStackTrace();
        }
    }
}

