package org.minecraft.itemchecker.baseclasses;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class YamlData {
    public String name;
    public String checkNBT;
    public List<CheckList> checkLists = new ArrayList<>();
    public YamlData(String Name, String CheckNBT){
        name = Name;
        checkNBT = CheckNBT;
    }
    public String getCommand(Player p, int index){
        String returnString = checkLists.get(index).consoleCommand;
        returnString = returnString.replace("%player%",p.getName());
        return returnString;
    }
    public int CheckContainedText(String str){
        for (int i = 0;i<checkLists.size();i++){
            if(str.contains(checkLists.get(i).containedText)){
                return i;
            }
        }
        return -1;
    }
}
