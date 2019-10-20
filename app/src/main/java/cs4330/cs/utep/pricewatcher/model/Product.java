package cs4330.cs.utep.pricewatcher.model;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class holds information about the current product and changes done to
 * it.
 *
 * @author Isaias Leos
 */
public class Product {

    private String url;
    private String name;
    private double currentPrice;
    private double change;
    private double startingPrice;
    private String date;

    /**
     * Default Constructor
     */
    public Product() {
    }

    /**
     * Creates a product given all the parameters.
     *
     * @param url           the url pertaining to the product
     * @param name          name of the product
     * @param currentPrice  current price of the product
     * @param change        change between previous price and current
     * @param startingPrice initial price of the product
     */
    public Product(String url, String name, double currentPrice, double startingPrice, double change) {
        this.date = getCurrentDate();
        this.url = url;
        this.name = name;
        this.currentPrice = currentPrice;
        this.change = change;
        this.startingPrice = startingPrice;
        if (this.url.contains("amazon")) {
            urlSanitize();
        }
    }

    /**
     * Sets the price to a new calculated price and updates the change.
     *
     * @param price price of the product
     */
    public void checkPrice(double price) {
        setCurrentPrice(price);
        setChange(new BigDecimal(calcChange(getCurrentPrice(), getInitialPrice())).setScale(2, RoundingMode.CEILING).doubleValue());
    }

    /**
     * This method computes the percentage difference between two numbers.
     *
     * @param initialPrice initial price of the product
     * @param newPrice     the current price of the product
     * @return the difference shown as a percentage
     */
    private double calcChange(double newPrice, double initialPrice) {
        return ((newPrice - initialPrice) / initialPrice) * 100;
    }

    public String getDate() {
        return date;
    }

    /**
     * Gets the current date.
     *
     * @return current date in MM/dd/yyyy format
     */
    private String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    /**
     * Sets a boolean that indicates if the current Product can play a sound.
     *
     * @return returns the current URL of the product being watched.
     */
    public String getURL() {
        return url;
    }

    /**
     * Replaces the current URL of the product being watched.
     *
     * @param url the name of the URL that will be replacing the current item
     *            name.
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * @return returns the name of the current product.
     */
    public String getName() {
        return name;
    }

    /**
     * Replaces the current Name of the product being watched.
     *
     * @param name the name of the product that will be replacing the current item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return returns the price of the current product.
     */
    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Replaces the current Price of the product being watched.
     *
     * @param price the price that will be replacing the current price of the
     *              product.
     */
    public void setCurrentPrice(double price) {
        this.currentPrice = price;
    }


    /**
     * @return the value of change between the initial price and the new price.
     */
    public double getChange() {
        return change;
    }

    /**
     * Sets the value of difference between two prices.
     *
     * @param change get the change in product prices
     */
    public void setChange(double change) {
        this.change = change;
    }

    /**
     * Get the initial price of a product.
     *
     * @return starting price
     */
    public double getInitialPrice() {
        return startingPrice;
    }

    /**
     * Sets the initial price of an product.
     *
     * @param price override the starting price of the product
     */
    public void setStartingPrice(double price) {
        this.startingPrice = price;
    }

    /**
     * Removes unwanted information from an Amazon Link. Enabling web scraping
     * to perform better.
     */
    private void urlSanitize() {
        String[] sanitize = url.split("/");
        StringBuilder newUrl = new StringBuilder();
        for (int i = 0; i < sanitize.length; i++) {
            if (sanitize[i].contains("ref") && !sanitize[i].contains("?ref")) {
                sanitize[i] = "";
            }
            if (sanitize[i].contains("?ref")) {
                String[] fixURL = sanitize[i].split("[?]");
                sanitize[i] = fixURL[0];
            }
            if (sanitize[i].equals("/") || sanitize[i].equals("//")) {
                sanitize[i] = "";
            }
            newUrl.append(sanitize[i]).append("/");
        }
        if (newUrl.toString().contains("//")) {
            newUrl = new StringBuilder(newUrl.substring(0, newUrl.length() - 1));
        }
        setURL(newUrl.toString());
    }
}