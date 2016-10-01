package com.kilo.microkit.core.provider;

import com.kilo.microkit.api.sample.AffiliateAPIException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kraghunathan on 10/1/16.
 */
public class HTTP {

    private String affiliateToken;

    public static void main(String[] args) {
        String s = "https://affiliate-api.flipkart.net/affiliate/feeds/goingkilo/category/7jv.json?expiresAt=1475327344645&sig=d9162dbb67e1090af4977c7e70503043";

    }

    public String queryService(String urlString) throws AffiliateAPIException {

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Fk-Affiliate-Token", getAffiliateToken());
            con.setRequestProperty("Fk-Affiliate-Id", getAffiliateId());

            int status = con.getResponseCode();

            switch (status) {

                case HttpURLConnection.HTTP_GONE:
                    // The timestamp is expired.
                    throw new AffiliateAPIException("URL expired");

                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    // The API Token or the Tracking ID is invalid.
                    throw new AffiliateAPIException("API Token or Affiliate Tracking ID invalid.");

                case HttpURLConnection.HTTP_FORBIDDEN:
                    // Tampered URL, i.e., there is a signature mismatch.
                    // The URL contents are modified from the originally returned value.
                    throw new AffiliateAPIException("Tampered URL - The URL contents are modified from the originally returned value");

                case HttpURLConnection.HTTP_OK:

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return response.toString();

                default:
                    throw new AffiliateAPIException("Connection error with the Affiliate API service: HTTP/" + status);
            }
        } catch (MalformedURLException mfe) {
        } catch (IOException ioe) {
        }

        return "";
    }

    public String getAffiliateToken() {
        return "1368e5baaf8e4bcdb442873d4aa8ef6e";
    }

    public String getAffiliateId() {
        return "goingkilo";
    }
}
