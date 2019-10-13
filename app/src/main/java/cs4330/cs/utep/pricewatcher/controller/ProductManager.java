package cs4330.cs.utep.pricewatcher.controller;

import java.util.ArrayList;
import java.util.List;

import cs4330.cs.utep.pricewatcher.model.Product;

public class ProductManager {

    private List<Product> arrOfProducts;
    private PriceFinder priceFinder = new PriceFinder();

    /**
     * Generates a Empty list of Products.
     */
    public ProductManager() {
        this.arrOfProducts = new ArrayList<>();
    }

    /**
     * Obtain new price for every item inside the ProductManager.
     */
    public void reloadManager() {
        for (Product temp : arrOfProducts) {
            temp.checkPrice(priceFinder.getPrice(temp.getCurrentPrice()));
        }
    }

    /**
     * Adds a Product to the Product List.
     *
     * @param product product
     */
    public void add(Product product) {
        this.get().add(product);
    }

    /**
     * Deletes a Product from the Product List.
     *
     * @param product product
     */
    public void delete(int product) {
        this.get().remove(product);
    }

    /**
     * @return the list of products
     */
    public List<Product> get() {
        return this.arrOfProducts;
    }

    /**
     * Creates a Product and adds it to the existing ArrayList.
     *
     * @param name          name of product
     * @param currentPrice  current price of the product
     * @param startingPrice initial price of the product
     * @param change        the change between previous price and current
     */
    public void create(String name, String url, double currentPrice, double startingPrice, double change) {
        Product generated = new Product();
        generated.setName(name);
        generated.setURL(url);
        generated.setCurrentPrice(currentPrice);
        generated.setStartingPrice(startingPrice);
        generated.setChange(change);
        add(generated);
    }

    /**
     * Sets the current product list to the given one from the parameter.
     *
     * @param arrOfProducts a list of products
     */
    public void set(List<Product> arrOfProducts) {
        this.arrOfProducts = arrOfProducts;
    }

    /**
     * Removes all Product's from the existing ArrayList.
     */
    public void remove() {
        this.arrOfProducts = new ArrayList<>();
    }

}
