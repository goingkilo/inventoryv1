package com.kilo.microkit.api.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kilo.microkit.api.model.InventoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kraghunathan on 9/30/16.
 */
public class Parser {

    public static List<InventoryItem> parseSearchResults(String json) {
        List<InventoryItem> ret = new ArrayList<InventoryItem>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readValue(json.getBytes(), JsonNode.class);
            for( int i  = 0 ; i < rootNode.elements().next().size() ; i++ ) {
                JsonNode item = rootNode.elements().next().get(i);
                InventoryItem p = new InventoryItem();

                p.setId( item.findValue("productId").asText());
                p.setCategory( item.findValue("categoryPaths").findValue("title").asText());
                p.setTitle( item.findValue("productAttributes").findValue("title").asText());
                p.setDesc(item.findValue("productDescription").asText());
                p.setPrice(item.findValue("sellingPrice").findValue("amount").asText());
                p.setUrl(item.findValue("productUrl").asText());
                p.setColor(item.findValue("color").asText());
                p.setInStock(item.findValue("inStock").asText());

                String[] imgkeys = new String[]{"200x200","200x200","200x200","200x200","default","unknown"};
                for( String key : imgkeys ){
                    String img = item.findValue(key).asText();
                    if( img != null ) {
                        p.setImage(img);
                    }
                }

                ret.add(p);
            }

        }
        catch(Exception e){
            System.out.println( e.getMessage());
            System.out.println( e.getCause());
            System.out.println( json);
            e.printStackTrace();
        }
        return ret;
    }
}
