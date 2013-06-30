package com.turbogrimoire.purelysatanic.dynamicmarket.items;

import com.turbogrimoire.purelysatanic.dynamicmarket.transactions.Transaction;

public interface Item {
    
    /**
     * Returns an integer representing the item's unique ID.
     * 
     * @return the ID of the item
     */
    public int getId();
    
    /**
     * Returns a String object representing the item's name.
     * 
     * @return the name of the item
     */
    public String getName();
    
    /**
     * Returns a String object representing the item's alias if set, otherwise null.
     * 
     * @return the alias of the item
     */
    public String getAlias();
    
    /**
     * Returns a boolean representing whether the item can be purchased.
     * 
     * @return whether the item can be purchased
     */
    public boolean isPurchasable();
    
    /**
     * Returns a boolean representing whether the item can be sold
     * 
     * @return whether the item can be sold
     */
    public boolean isSellable();
    
    /**
     * Returns a double representing the item's base calculation price.
     *
     * @return the base price
     */
    public double getBasePrice();

    /**
     * Returns a double representing the item's maximum selling price.
     * 
     * @return the max selling price
     */
    public double getPriceCeiling();

    /**
     * Returns a double representing the item's minimum purchase price.
     * 
     * @return the minimum purchase price
     */
    public double getPriceFloor();
    
    /**
     * Returns an integer representing the item's maximum stock.
     * 
     * @return the maximum stock
     */
    public int getStockCeiling();

    /**
     * Returns an integer representing the item's minimum stock.
     * 
     * @return the minimum stock
     */
    public int getStockFloor();

    /**
     * Returns an integer representing the item's current stock.
     * 
     * @return the current stock
     */
    public int getStock();

    /**
     * Returns an integer representing the item's volatility.
     * 
     * @return the volatility
     */
    public int getVolatility();

    /**
     * Returns an integer representing the item's sales tax.
     * 
     * @return the sales tax
     */
    public int getSalesTax();

    /**
     * Returns an double representing the item's selling price the previous day.
     * 
     * @return the previous day's selling price
     */
    public double getPreviousSellPrice();

    /**
     * Returns a double representing the item's purchase price based on quantity
     *  
     * @param quantity the amount being purchased
     * @return the purchase price
     */
    public double getPurchasePrice(int quantity);

    /**
     * Returns a double representing the item's selling price based on quantity
     * 
     * @param quantity the amount being sold
     * @return the selling price
     */
    public double getSalePrice(int quantity);
    
    /**
     * This method will be called upon meeting all requirements for the purchase to complete.
     * It will implement the item specific purchasing functionality.
     * 
     * @param transaction the transaction being completed
     */
    public void onPurchase(Transaction transaction);
    
    /**
     * This method will be called upon meeting all requirements for the sale to complete.
     * The implementation will be child unique.
     * 
     * @param transaction the transaction being completed
     */
    public void onSale(Transaction transaction);

}
