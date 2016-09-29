package com.kilo.microkit.api.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kilo.microkit.api.APIFeeds;
import com.kilo.microkit.api.AffiliateAPIException;
import com.kilo.microkit.api.ProductInfo;
import com.kilo.microkit.api.model.InventoryItem;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kraghunathan on 9/8/16.
 */
public class FlipKart {

	public static final String search = "https://affiliate-api.flipkart.net/affiliate/search/json?query=searchTerm&resultCount=Count";

	static APIFeeds feeds = new APIFeeds("goingkilo", "1368e5baaf8e4bcdb442873d4aa8ef6e", "no");


	public static List<InventoryItem> search(Client client, String searchTerm,  int count) throws SocketTimeoutException {

		String s = search.replace("searchTerm", searchTerm).replace("=Count", "=" + count);
		return  parse(get(client, s, count));
	}

	public static Map<String,String> categories() {

		Map<String, String> categories = null;
		try {
			categories = feeds.categories();
		} catch (AffiliateAPIException e) {
			e.printStackTrace();
		}
		return categories;
	}

	public static List<ProductInfo> products(String category) {
		List<ProductInfo> productInfos = null;
		try {
			productInfos = feeds.products(category);
		} catch (AffiliateAPIException e) {
			e.printStackTrace();
		}
		return productInfos;
	}



	public static String get(Client client, String query, int count) throws SocketTimeoutException {
		String s = query;

		System.out.println( "searching for : " + s);

		try {
			String json = client.target(s)
					.request(MediaType.APPLICATION_JSON)
					.header("Fk-Affiliate-Id", "goingkilo")
					.header("Fk-Affiliate-Token", "1368e5baaf8e4bcdb442873d4aa8ef6e")
					.get(String.class);
			return json ;
		}
		catch(RuntimeException e) {
			System.out.println(" >>>>> ");
		}
		return "";
	}

	public static List<InventoryItem> parse(String json) {
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

				String[] img = new String[4];
				img[0] = item.findValue("200x200").asText();
				img[0] = item.findValue("400x400").asText();
				img[0] = item.findValue("800x800").asText();
				img[0] = item.findValue("unknown").asText();
				p.setImages( img);

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

	public static void main(String[] args) {
		Map<String, String> a = categories();
		for( String s : a.keySet()) {
			System.out.println( s );
			System.out.println( a.get(s) );
		}
		List<ProductInfo> b = products("desktops");

		for(ProductInfo x : b) {
			System.out.println(x);
		}
	}

}
