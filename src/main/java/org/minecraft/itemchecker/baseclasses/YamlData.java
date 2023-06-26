package org.minecraft.itemchecker.baseclasses;

import org.bukkit.entity.Player;

public class YamlData {
    public String name;
    public String checkNBT;
    public String containedText;
    public String consoleCommand;
    public YamlData(String Name, String CheckNBT, String ContainedText, String ConsoleCommand){
        name = Name;
        checkNBT = CheckNBT;
        containedText = ContainedText;
        consoleCommand = ConsoleCommand;
    }
    public String getCommand(Player p){
        String returnString = consoleCommand;
        returnString = returnString.replace("%player%",p.getName());
        return returnString;
    }
}
