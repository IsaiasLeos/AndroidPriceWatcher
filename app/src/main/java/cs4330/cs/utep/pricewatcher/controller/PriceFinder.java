package cs4330.cs.utep.pricewatcher.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class PriceFinder {

    /**
     * Generate a simulated price of an product between the value given.
     *
     * @param medValue value to randomize within ranges
     * @return random double
     */
    public double getPrice(Double medValue) {
        Random rand = new Random();
        double minValue = medValue - (medValue / 10);
        double maxValue = medValue + (medValue / 10);
        return (new BigDecimal(minValue + (maxValue - minValue) * rand.nextDouble()).setScale(2, RoundingMode.CEILING).doubleValue());
    }
}