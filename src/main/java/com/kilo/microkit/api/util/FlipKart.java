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



	public static final String FK_AFFILIATE_ID_KEY = "Fk-Affiliate-Id";
	public static final String FK_AFFILIATE_ID_VAL = "goingkilo";
	public static final String FK_AFFILIATE_TOKEN_KEY = "Fk-Affiliate-Token";
	public static final String FK_AFFILIATE_TOKEN_VAL = "1368e5baaf8e4bcdb442873d4aa8ef6e";

	final static String search = "https://affiliate-api.flipkart.net/affiliate/search/json?query=searchTerm&resultCount=Count";

	public static String  get(Client client) {
		return  client.target("https://affiliate-api.flipkart.net/affiliate/api/goingkilo.json")
				.request(MediaType.TEXT_PLAIN)
				.header(FK_AFFILIATE_ID_KEY, FK_AFFILIATE_ID_VAL)
				.header(FK_AFFILIATE_TOKEN_KEY, FK_AFFILIATE_TOKEN_VAL)
				.get(String.class);
	}

	public static List<InventoryItem> search(Client client, String searchTerm, int count) throws SocketTimeoutException {

		String s = search.replace("searchTerm", searchTerm)
                .replace("=Count", "=" + count);
		System.out.println( "searching for : " + s);
				//System.out.println(s);
		try {
			String json = client.target(s)
					.request(MediaType.APPLICATION_JSON)
					.header(FK_AFFILIATE_ID_KEY, FK_AFFILIATE_ID_VAL)
					.header(FK_AFFILIATE_TOKEN_KEY, FK_AFFILIATE_TOKEN_VAL)
					.get(String.class);
			return parse(json);
		}
		catch(RuntimeException e) {
			System.out.println(" >>>>> ");
		}
		return new ArrayList<InventoryItem>();
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

//				out.println(p);
//				out.println("-----------------------------");
				ret.add(p);
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}



    /*
    products = 'https://affiliate-api.flipkart.net/affiliate/api/goingkilo.json'
	products = 'http://affiliate-api.flipkart.net/affiliate/api/goingkilo.json'
	deals =    'https://affiliate-api.flipkart.net/affiliate/offers/v1/dotd/json'
	offers =   'https://affiliate-api.flipkart.net/affiliate/offers/v1/top/json'
	search_product = 'https://affiliate-api.flipkart.net/affiliate/product/json?id='
	search = 'https://affiliate-api.flipkart.net/affiliate/search/json?query=searchTerm&resultCount=Count'
	search = 'http://affiliate-api.flipkart.net/affiliate/search/json?query=searchTerm&resultCount=Count'
	orders_report = 'https://affiliate-api.flipkart.net/affiliate/report/orders/detail/json?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd&status=<status>&offset=0'

def get(url):
	req = urllib2.Request(url)
	req.add_header('Fk-Affiliate-Id', 'goingkilo')
	req.add_header('Fk-Affiliate-Token', '1368e5baaf8e4bcdb442873d4aa8ef6e')
	resp = urllib2.urlopen(req)
	content = resp.read()

	return json.loads(content)

     */
}
