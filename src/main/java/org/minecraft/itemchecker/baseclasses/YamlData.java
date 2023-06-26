package org.minecraft.itemchecker.baseclasses;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class YamlData {
    public String name;
    public boolean useRegularNBT;
    public String checkNBT;
    public List<CheckList> checkLists = new ArrayList<>();
    public YamlData(String Name, boolean UseRegularNBT, String CheckNBT){
        name = Name;
        useRegularNBT = UseRegularNBT;
        checkNBT = CheckNBT;
    }
    public String getCommand(Player p, int index){
        String returnString = checkLists.get(index).consoleCommand;
        if(returnString == null) return null;
        returnString = returnString.replace("%player%",p.getName());
        return returnString;
    }
    public int CheckContainedText(String str){
        for (int i = 0;i<checkLists.size();i++){
            CheckList checkList = checkLists.get(i);
            if(checkList.containedText != null) {
                if (checkList.useRegularText == false) {
                    if (str.contains(checkList.containedText)) {
                        return i;
                    }
                } else {
                    if (str.matches(checkList.containedText)) {
                        return i;
                    }
                }
            }
            else{
                return i;
            }
        }
        return -1;
    }
}
