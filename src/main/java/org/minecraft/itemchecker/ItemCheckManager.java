package org.minecraft.itemchecker;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.minecraft.itemchecker.baseclasses.YamlData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ItemCheckManager {

    public List<YamlData> list = new ArrayList<>();
    public ItemCheckManager(){

    }
    public void LoadFile(){
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("ItemChecker").getDataFolder(), File.separator + "Settings");
        try {
            File defaultFile = new File(userdata, File.separator + "#default.yml");
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultFile);
            defaultConfig.createSection("Default");
            defaultConfig.set("Default.CheckNBT", "test");
            defaultConfig.set("Default.ContainedText", "testitem");
            defaultConfig.set("Default.ConsoleCommand", "kill %player%");
            defaultConfig.save(defaultFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        list.clear();
        try{
            for (File f: userdata.listFiles()) {
                if((f.isFile() || f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(".")) == ".yml") &&
                        f.getName().charAt(0) != '#'){
                    FileConfiguration config;
                    config = YamlConfiguration.loadConfiguration(f);
                    for (String name: getFirstTag(f)) {
                        list.add(new YamlData(
                                name,
                                config.getString(name + ".CheckNBT","null"),
                                config.getString(name + ".ContainedText","null"),
                                config.getString(name + ".ConsoleCommand","null")
                        ));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public YamlData ContainsNBT(String str){
        for (YamlData data: list) {
            if(str.contains(data.checkNBT)){
                return data;
            }
        }
        return null;
    }

    public void CheckItem(Player p, ItemStack stack, int slot){
        NBTItem nbtItem = new NBTItem(stack);
        if (stack != null && stack.getItemMeta() != null) {
            String itemName = stack.getItemMeta().getDisplayName();
            if (nbtItem.hasNBTData() != false) {
                String nbtStr = nbtItem.toString();
                YamlData data = ContainsNBT(nbtStr);
                if(data != null) {
                    if (itemName.equalsIgnoreCase(data.containedText)) {
                        p.getInventory().setItem(slot, new ItemStack(Material.AIR));
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        Bukkit.dispatchCommand(console, data.getCommand(p));
                    }
                }
            }
        }
    }

    private List<String> getFirstTag(File yamlFile){
        List<String> list = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(yamlFile));
            String line = br.readLine();
            while (line != null) {
                if(line.charAt(0) != ' ' && line.charAt(0) != '\t' && line.charAt(0) != '#'){
                    list.add(line.substring(0,line.length()-1));
                }
                line = br.readLine();
            }
            br.close();
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
