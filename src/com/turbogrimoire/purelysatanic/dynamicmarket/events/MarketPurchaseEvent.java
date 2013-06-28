package com.turbogrimoire.purelysatanic.dynamicmarket.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/*
 * This class represents the occurrence of a player purchasing a market item
 * other plugins must be able to cancel this event.
 */
public class MarketPurchaseEvent extends Event implements Cancellable {

    @Override
    public HandlerList getHandlers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isCancelled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setCancelled(boolean arg0) {
        // TODO Auto-generated method stub
        
    }

}
