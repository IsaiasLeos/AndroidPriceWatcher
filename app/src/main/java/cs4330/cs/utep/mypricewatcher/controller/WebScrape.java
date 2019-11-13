package cs4330.cs.utep.mypricewatcher.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import cs4330.cs.utep.mypricewatcher.model.Product;

/**
 * @author Isaias Leos
 */
class WebScrape {

    /**
     * Regex to find the price within an HTML source input.
     *
     * @param input
     * @return
     */
    private static String findPrice(String input) {
        String output = "";
        Pattern pattern = Pattern.compile("[$](([1-9]+\\.?\\d*)|([0]\\.\\d*)|[0])");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            output = matcher.group();
        }
        if (input.contains(",")) {
            String[] tokens = input.split("[>=]");
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equals("\"\" content")) {
                    output = tokens[i + 1];
                    output = output.replaceAll("[\"]", "");
                }
            }
        }
        return output;
    }


    /**
     * Call individual methods for scrapping different websites
     *
     * @param product
     * @return
     */
    Product priceScrape(Product product) {
        if (product.getURL().contains("ebay")) {
            Ebay(product);
        } else if (product.getURL().contains("amazon")) {
            scrapeAmazon(product);
        } else if (product.getURL().contains("walmart")) {
            Walmart(product);
        }
        if (product.getInitialPrice() != 0.00) {
            product.checkPrice(product.getCurrentPrice());
        }
        return product;
    }

    /**
     * Obtains the price of an item using RegEx. Contains $, Followed by Digits,
     * then a period or any number of digits before the period. After the period
     * any number of digits.
     */
    private void scrapeAmazon(Product product) {
        HttpURLConnection con;
        String output;
        try {
            URL url = new URL(product.getURL());
            con = (HttpURLConnection) url.openConnection();
            String encoding = con.getContentEncoding();
            if (encoding == null) {
                encoding = "utf-8";
            }
            InputStreamReader reader;
            if ("gzip".equals(encoding)) {
                reader = new InputStreamReader(new GZIPInputStream(con.getInputStream()));
            } else {
                reader = new InputStreamReader(con.getInputStream(), encoding);
            }
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                output = findPrice(line);
                if (line.contains("newBuyBoxPrice")) {
                    if (product.getInitialPrice() == 0.00) {
                        product.setInitialPrice(Double.parseDouble(output.substring(1)));
                        product.setCurrentPrice(Double.parseDouble(output.substring(1)));
                        product.setChange(0.00);
                        return;
                    } else {
                        product.checkPrice(Double.parseDouble(output.substring(1)));
                        return;
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setCurrentPrice(-1.00);
    }

    /**
     * Obtains the price of an item using JSOUP. Contains $, Followed by Digits,
     * then a period or any number of digits before the period. After the period
     * any number of digits.
     */
    private void Ebay(Product product) {
        String output;
        Document doc;
        try {
            doc = Jsoup.connect(product.getURL()).get();
            output = findPrice(String.valueOf(doc.getElementById("prcIsum")));
            if (product.getInitialPrice() == 0.00) {
                product.setInitialPrice(Double.parseDouble(output.substring(1)));
                product.setCurrentPrice(Double.parseDouble(output.substring(1)));
                product.setChange(0.00);
                return;
            } else {
                product.checkPrice(Double.parseDouble(output.substring(1)));
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setCurrentPrice(-1.00);
    }

    /**
     * Obtains the price of an item using JSoup. Contains $, Followed by Digits,
     * then a period or any number of digits before the period. After the period
     * any number of digits.
     */
    private void Walmart(Product product) {
        String output;
        Document doc;
        try {
            doc = Jsoup.connect(product.getURL()).get();
            output = findPrice(String.valueOf(doc.getElementById("price")));
            if (product.getInitialPrice() == 0.00) {
                product.setInitialPrice(Double.parseDouble(output.substring(1)));
                product.setCurrentPrice(Double.parseDouble(output.substring(1)));
                product.setChange(0.00);
                return;
            } else {
                product.checkPrice(Double.parseDouble(output.substring(1)));
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setCurrentPrice(-1.00);
    }

}