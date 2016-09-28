package com.kilo.microkit.api.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kilo.microkit.api.model.InventoryItem;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kraghunathan on 9/8/16.
 */
public class FlipKart {

	public static final String products = "https://affiliate-api.flipkart.net/affiliate/api/goingkilo.json";
	public static final String deals =    "https://affiliate-api.flipkart.net/affiliate/offers/v1/dotd/json";
	public static final String offers =   "https://affiliate-api.flipkart.net/affiliate/offers/v1/all/json";
	public static final String search_product = "https://affiliate-api.flipkart.net/affiliate/product/json?id=";
	public static final String search = "https://affiliate-api.flipkart.net/affiliate/search/json?query=searchTerm&resultCount=Count";
	public static final String  orders_report = "https://affiliate-api.flipkart.net/affiliate/report/orders/detail/json?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd&status=<status>&offset=0";


	public static List<InventoryItem> search(Client client, String searchTerm,  int count) throws SocketTimeoutException {

		String s = search.replace("searchTerm", searchTerm).replace("=Count", "=" + count);
		return  parse(get(client, s, count));
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

	static List<InventoryItem> parse(String json) {
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


}
