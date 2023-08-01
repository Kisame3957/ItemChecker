package org.minecraft.itemchecker.baseclasses;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheckListItem {
    public String name;
    public boolean useRegularText;
    public String containedText;
    public String consoleCommand;
    public CheckListItem(){

    }
    public CheckListItem(String Name, boolean UseRegularText, String ContainedText, String ConsoleCommand){
        name = Name;
        useRegularText = UseRegularText;
        containedText = ContainedText;
        consoleCommand = ConsoleCommand;
    }

    public String getCommand(Player p, ItemStack i){
        if(consoleCommand == null) return null;
        return consoleCommand;
    }
}
