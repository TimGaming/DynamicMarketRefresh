package com.turbogrimoire.purelysatanic.dynamicmarket.items;

import java.text.DecimalFormat;


/**
 * @author PurelySatanic
 *
 * This class implements shared functionality between all Items, may be overwritten by child class.
 * 
 * The algorithm used to determine purchasing and selling price originated from a plugin of the same name,
 * which can be found here: http://forums.bukkit.org/threads/inactive-econ-dynamicmarket-v-0-4-8-1-versatile-shop-plugin-440-480.3266/
 * All credit goes to the respective authors.
 *
 */
public abstract class AbstractItem implements Item {

    private final int intScale = 10000; // Price scaling
    private final int id;              // Unique field
    protected String  name;
    protected String  alias;           // Does not need to be set
    private boolean   purchasable;
    private boolean   sellable;
    private double    basePrice;
    private double    priceCeiling;
    private double    priceFloor;
    private int       stockCeiling;
    private int       stockFloor;
    private int       stock;
    private int       volatility;
    private int       salesTax;

    public AbstractItem(int id, String name, String alias, boolean purchasable, boolean sellable,
                        double basePrice, double priceCeiling, double priceFloor, int stockCeiling,
                        int stockFloor, int stock, int volatility, int salesTax,
                        double previousPrice) {

        this.id = id;
        this.name = name;
        this.alias = alias;
        this.purchasable = purchasable;
        this.sellable = sellable;
        this.basePrice = basePrice;
        this.priceCeiling = priceCeiling;
        this.priceFloor = priceFloor;
        this.stockCeiling = stockCeiling;
        this.stockFloor = stockFloor;
        this.stock = stock;
        this.volatility = volatility;
        this.salesTax = salesTax;

        check();

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public boolean isPurchasable() {
        return purchasable;
    }

    @Override
    public boolean isSellable() {
        return sellable;
    }

    @Override
    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public double getPriceCeiling() {
        return priceCeiling;
    }

    @Override
    public double getPriceFloor() {
        return priceFloor;
    }

    @Override
    public int getStockCeiling() {
        return stockCeiling;
    }

    @Override
    public int getStockFloor() {
        return stockFloor;
    }

    @Override
    public int getStock() {
        return stock;
    }

    @Override
    public int getVolatility() {
        return volatility;
    }

    @Override
    public int getSalesTax() {
        return salesTax;
    }

    protected boolean addStock(int quantity) {
        stock += quantity;
        if (stock > stockCeiling)
            stock = stockCeiling;
        return true;
    }

    protected boolean subtractStock(int quantity) {
        stock -= quantity;
        if (stock < stockFloor)
            stock = stockFloor;
        return true;
    }

    protected boolean hasEnoughStock(int quantity) {

        if (stockFloor == Integer.MIN_VALUE)
            return true;
        else if (stock - quantity < stockFloor)
            return false;
        else
            return true;

    }

    protected boolean hasRoomForStock(int quantity) {

        if (stockCeiling == Integer.MAX_VALUE)
            return true;
        else if (stock + quantity > stockCeiling)
            return false;
        else
            return true;

    }

    private void check() {

        // Check and fix possible chaos-inducing data.
        // Mirrors DatabaseMarket.sanityCheckAll.

        this.salesTax = rangeCrop(this.salesTax, 0, 100);

        if (this.stockCeiling < this.stock)
            this.stockCeiling = this.stock;

        if (this.stockFloor > this.stock)
            this.stockFloor = this.stock;

        if (this.priceCeiling < this.priceFloor)
            this.priceCeiling = this.priceFloor;

        if (this.priceCeiling > Integer.MAX_VALUE)
            this.priceCeiling = Integer.MAX_VALUE;

        this.basePrice = Math.max(0, this.basePrice);

    }

    @Override
    public double getPurchasePrice(int amount) {
        // Return the purchase price of the given number of bundles.
        return round(getBatchPrice(stock, stock - amount + 1), 2);
    }

    @Override
    public double getSalePrice(int amount) {
        // Return the selling price of the given number of bundles.
        return round(deductTax(getBatchPrice(stock, stock + amount - 1)), 2);
    }

    // Price Calculations from original Dynamic Market
    private static int rangeCrop(int value, int minVal, int maxVal) {
        return (Math.min(Math.max(value, minVal), maxVal));
    }

    private static double rangeCrop(double value, double minVal, double maxVal) {
        return (Math.min(Math.max(value, minVal), maxVal));
    }

    private double getBatchPrice(int startStock, int endStock) {
        // Gets the current base price for the items at stock levels from
        // startStock to endStock.
        // NOTE: Does not check stockLowest/stockHighest transaction limits.

        int lowStock = Math.min(startStock, endStock);
        int highStock = Math.max(startStock, endStock);
        int numTerms = highStock - lowStock + 1;
        double lowStockPrice;
        double highStockPrice;
        int fixedStockLimit;

        // End calculation if volatility == 0. Price does not change, so all
        // items are the same value.
        if (volatility == 0)
            return (numTerms * getStockPrice(stock));

        // End calculation if highStock <= stockFloor (All below floor)
        if (highStock <= stockFloor)
            return (numTerms * getStockPrice(stockFloor));

        // End calculation if lowStock >= stockCeil (All above ceiling)
        if (lowStock >= stockCeiling)
            return (numTerms * getStockPrice(stockCeiling));

        // Split calculation if stockFloor reached by lowStock (Some below
        // floor)
        if (lowStock < stockFloor)
            return (((stockFloor - lowStock + 1) * getStockPrice(stockFloor)) + getBatchPrice(stockFloor + 1,
                                                                                              highStock));

        // Split calculation if stockCeil reached by highStock (Some above
        // ceiling)
        if (highStock > stockCeiling)
            return (((highStock - stockCeiling + 1) * getStockPrice(stockCeiling)) + getBatchPrice(lowStock,
                                                                                                   stockCeiling - 1));

        lowStockPrice = getStockPrice(lowStock); // highest price in range
        highStockPrice = getStockPrice(highStock); // lowest price in range

        // WARNING in this section: Highest stock level corresponds to lowest
        // price,
        // and lowest stock level corresponds to highest price.

        // End calculation if lowStockPrice <= priceFloor (All below floor)
        if (lowStockPrice <= priceFloor)
            return (numTerms * priceFloor);

        // End calculation if highStockPrice >= priceCeil (All above ceiling)
        if (highStockPrice >= priceCeiling)
            return (numTerms * priceCeiling);

        // Split calculation if highStockPrice < priceFloor (Some below floor)
        if (highStockPrice < priceFloor) {
            fixedStockLimit = (int) Math.round(Math.floor(stockAtPrice(priceFloor)));
            return (((highStock - fixedStockLimit + 1) * priceFloor) + getBatchPrice(lowStock,
                                                                                     fixedStockLimit - 1));
        }

        // Split calculation if lowStockPrice > priceCeil (Some above ceiling)
        if (lowStockPrice > priceCeiling) {
            fixedStockLimit = (int) Math.round(Math.ceil(stockAtPrice(priceCeiling)));
            return (((fixedStockLimit - lowStock + 1) * priceCeiling) + getBatchPrice(fixedStockLimit + 1,
                                                                                      highStock));
        }

        // All range limits handled? Find the sum of terms of a finite geometric
        // series.
        // return Math.round(this.basePrice * Math.pow(getVolFactor(),-lowStock)
        // * (Math.pow(getVolFactor(),numTerms) - 1) / (getVolFactor()-1));
        // return math.round(firstTerm * (1 - (ratio ^ terms)) / (1 - ratio));
        return round(lowStockPrice * (1 - (Math.pow(1 / getVolFactor(), numTerms)))
                     / (1 - (1 / getVolFactor())), 2);
    }

    private double deductTax(double basePrice) {
        // Returns the given price minus the sales tax.
        return (basePrice * (1 - ((double) salesTax / 100)));
    }

    private double getStockPrice(int stockLevel) {
        // Crops result to stockFloor/stockCeil/priceFloor/priceCeil.
        return rangeCrop(this.basePrice
                         * Math.pow(getVolFactor(),
                                    -rangeCrop(stockLevel, stockFloor, stockCeiling)), priceFloor,
                         priceCeiling);
    }

    private double getVolFactor() {
        return (1 + (double) volatility / intScale);
    }

    private double stockAtPrice(double targPrice) {
        // Returns the stock level at which price == targPrice.
        if (volatility == 0) {
            // If price doesn't change, the stock level is effectively +/-INF.
            if (targPrice > basePrice)
                return Double.MIN_VALUE;
            if (targPrice < basePrice)
                return Double.MAX_VALUE;
            // targPrice == basePrice
            return stock;
        }
        return -(Math.log(targPrice / basePrice) / Math.log(getVolFactor()));
    }

    private double round(double number, int precision) {
        String format = "#.";
        for (int i = 0; i < precision; i++) {
            format += "#";
        }
        DecimalFormat df = new DecimalFormat(format);
        return Double.valueOf(df.format(number));
    }

}
