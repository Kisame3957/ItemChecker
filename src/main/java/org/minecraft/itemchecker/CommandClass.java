//package org.minecraft.itemchecker;

//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;

//public class CommandClass implements CommandExecutor {
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if(command.getName().equalsIgnoreCase("itemchecker")){
//            if (args.length == 0) {
//                icm.LoadFile((Player)sender);
//                sender.sendMessage("Load");
//            } else {
//                if (args[0].equalsIgnoreCase("replace")) {
//                    icm.ShowList((Player)sender);
//                    sender.sendMessage("Show1");
//                } else {
//                    icm.ShowList((Player)sender);
//                    sender.sendMessage("Show2");
//                }
//            }
//            return true;
//        }
//        return false;
//    }
//}
