package org.minecraft.itemchecker.baseclasses;

public class CheckList {
    public String name;
    public boolean useRegularText;
    public String containedText;
    public String consoleCommand;
    public CheckList(){

    }
    public CheckList(String Name, boolean UseRegularText, String ContainedText,String ConsoleCommand){
        name = Name;
        useRegularText = UseRegularText;
        containedText = ContainedText;
        consoleCommand = ConsoleCommand;
    }
}
