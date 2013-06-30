package com.turbogrimoire.purelysatanic.dynamicmarket.transactions;

import org.bukkit.entity.Player;

import com.turbogrimoire.purelysatanic.dynamicmarket.items.Item;

public class Transaction {
    
    private final Player player;
    private final Type type;
    private final Item item;
    private final int quantity;
    private final long time;
    private Status status = Status.INVALID;
    
    public Transaction(Player player, Type type, Item item, int quantity) {
        
        this.player = player;
        this.type = type;
        this.item = item;
        this.quantity = quantity;
        this.time = System.currentTimeMillis();
        this.status = Status.PROCESSING;
        
    }
    
    /**
     * Returns a Player object representing the player involved with the transaction.
     * 
     * @return the player in the transaction
     */
    public Player getPlayer() {
        
        return this.player;
        
    }
    
    /**
     * Returns a Type enum representing the type of transaction.
     * 
     * @return the type of transaction
     */
    public Type getType() {
        
        return this.type;
        
    }
    
    /**
     * Returns an Item object representing the item involved with the transaction.
     * 
     * @return the item in the transaction
     */
    public Item getItem() {
        
        return this.item;
        
    }
    
    /**
     * Returns an integer representing the amount of the item involved with the transaction.
     * 
     * @return the quantity of the item in the transaction
     */
    public int getQuantity() {
        
        return this.quantity;
        
    }
    
    /**
     * Returns a long representing the time the transaction was started.
     * 
     * @return the time the transaction was started
     */
    public long getTime() {
        
        return this.time;
        
    }
    
    /**
     * Returns a Status enum representing the status of the transaction.
     * 
     * @return
     */
    public Status getStatus() {
        
        return status;
        
    }
    
    /**
     * Sets the status of the transaction.
     * 
     * @param status the status of the transaction
     */
    public void setStatus(Status status) {
        
        this.status = status;
        
    }

}
