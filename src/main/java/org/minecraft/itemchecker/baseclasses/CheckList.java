package org.minecraft.itemchecker.baseclasses;

public class CheckList {
    public String name;
    public String containedText;
    public String consoleCommand;
    public CheckList(){

    }
    public CheckList(String Name, String ContainedText,String ConsoleCommand){
        name = Name;
        containedText = ContainedText;
        consoleCommand = ConsoleCommand;
    }
}
