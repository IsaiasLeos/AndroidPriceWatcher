package cs4330.cs.utep.mypricewatcher.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import cs4330.cs.utep.mypricewatcher.model.Product;

/**
 * Author Isaias Leos
 */
public class PriceFinder extends WebScrape {

    /**
     * Obtain the price of an item, while also checking if the product didn't return a proper initial or current price.
     *
     * @param product
     * @param context
     * @return
     */
    public Product getPrice(Product product, Context context) {
        Product updated = priceScrape(product);
        if (product.getInitialPrice() == 0.0 || product.getCurrentPrice() == 0.0) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(context, "Failed to get " + product.getName() + "'s price. Please try again or let the developer know!", Toast.LENGTH_LONG).show());
        }
        return updated;
    }
}