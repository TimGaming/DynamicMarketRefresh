package com.turbogrimoire.purelysatanic.dynamicmarket.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/*
 * This class will handle all command calls for Dynamic Market.
 * After parsing, it will delegate the sub-commands to appropriate classes.
 */
public class MarketCommand implements CommandExecutor {

    private HelpCommand helpCommand = new HelpCommand(); // Handles help requests
    private PurchaseCommand purchaseCommand = new PurchaseCommand(); // Handles purchase requests
    private SellCommand sellCommand = new SellCommand(); // Handles sell requests
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        String subcommand; // This will take the value of args[0] if it exists
        
        if(sender instanceof ConsoleCommandSender) return false; // TO-DO: Handle server commands
        
        subcommand = args.length == 0 ? "help" : args[0].toLowerCase(); // Default to help if args[0] is null, else returns lowercase args[0]
        
        switch(subcommand) {
        case "help": return helpCommand.onCommand(sender, command, label, args);
        case "purchase": return purchaseCommand.onCommand(sender, command, label, args);
        case "sell": return sellCommand.onCommand(sender, command, label, args);
        }
        
        sender.sendMessage("Sorry, the command you entered was not recognized."); // If you have come this far, your command hasn't been delegated.
        return false;
        
    }

}
