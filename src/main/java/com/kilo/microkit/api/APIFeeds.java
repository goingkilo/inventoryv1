package com.kilo.microkit.api; /***
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
    }

    public DataParser getParser() {
        return parser;
    }


    public Map<String, String> categories() throws AffiliateAPIException {

        if (parser.initializeProductDirectory()) {
            return parser.getProductDirectory() ;
        }
        return null;
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

        /**
         * Usage: APIFeeds <AffiliateID> <AffiliateToken> <XML/JSON>
         */
        if(args.length < 3) {
            System.out.println(); System.out.println();
            System.out.println("Usage: APIFeeds <Affiliate ID> <Affiliate Token> <JSON>");
            System.out.println(); System.out.println();
            return;
        }

        try {
            if (args[2].equalsIgnoreCase("XML")) {
                //            parser = new XMLDataParser(affiliateId, affiliateToken);
                System.out.println("Usage: APIFeeds <Affiliate ID> <Affiliate Token> <JSON>");
                return;
            }
            APIFeeds feeds = new APIFeeds(args[0], args[1], args[2]);

            // Query the API service to get the list of categories and the corresponding URLs and store it
            // locally in productDirectory Map.
            if(feeds.getParser().initializeProductDirectory()) {

                System.out.println("Choose one of the categories:");
                // Get the list of categories from the locally stored productDirectory Map.
                Iterator<String> category_iterator = feeds.getParser().getProductDirectory().keySet().iterator();
                while(category_iterator.hasNext()) {
                    System.out.println(category_iterator.next());
                }
                System.out.print("Enter a category (or type 'q' to quit): ");
                Scanner s = new Scanner(System.in);
                String category;

                do {
                    category = s.nextLine();
                    if(category.equalsIgnoreCase("q")) { return; }

                    if(!feeds.getParser().getProductDirectory().keySet().contains(category)) {
                        System.out.print("Enter a valid category (or type 'q' to quit): ");
                    }
                    else {
                        break;
                    }
                } while(Boolean.TRUE);

                int count = 0;
                // Get a list of products for the given category.
                Iterator<ProductInfo>  products_iterator = feeds.getParser().getProductList(category).listIterator();
                while(products_iterator.hasNext()) {
                    ProductInfo product = products_iterator.next();
                    if(product.isInStock()) {
                        // Some of the fields are printed.
//                        System.out.println("Title: " + product.getTitle());
//                        System.out.println("URL: " + product.getProductUrl());
//                        System.out.println("Price: " + product.getMrp() + "\n\n");
                        System.out.println( product.toString());
                        count++;
                    }
                }

                System.out.println("Found " + count + " products in " + category + " category.\n\n");
            }
            else {
                System.out.println("Unable to contact the Flipkart Affiliate API service.");
            }
        }
        catch(AffiliateAPIException e) {
            System.out.println("API Exception raised: " + e.getMessage());
        }
    }
}

