package org.minecraft.itemchecker.baseclasses;

import java.util.ArrayList;
import java.util.List;

public class YamlData {
    public String name;
    public boolean useRegularNBT;
    public String checkNBT;
    public List<CheckListItem> checkListItems = new ArrayList<>();
    public YamlData(String Name, boolean UseRegularNBT, String CheckNBT){
        name = Name;
        useRegularNBT = UseRegularNBT;
        checkNBT = CheckNBT;
    }
    public CheckListItem CheckContainedText(String str){
        for (int i = 0; i < checkListItems.size(); i++) {
            CheckListItem checkListItem = checkListItems.get(i);
            if (checkListItem.containedText != null) {
                if (checkListItem.useRegularText == false) {
                    if (str.contains(checkListItem.containedText)) {
                        return checkListItem;
                    }
                } else {
                    if (str.matches(checkListItem.containedText)) {
                        return checkListItem;
                    }
                }
            } else {
                return checkListItem;
            }
        }
        return null;
    }
}
