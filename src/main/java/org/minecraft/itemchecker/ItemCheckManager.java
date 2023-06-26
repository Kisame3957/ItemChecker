package org.minecraft.itemchecker;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minecraft.itemchecker.baseclasses.CheckList;
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
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("itemchecker").getDataFolder(), File.separator + "settings");
        try {
            File defaultFile = new File(userdata, File.separator + "#default.yml");
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultFile);
            defaultConfig.createSection("default");
            defaultConfig.set("default.checknbt", "test");
            defaultConfig.createSection("default.checklist");
            defaultConfig.createSection("default.checklist.setting1");
            defaultConfig.set("default.checklist.setting1.containedtext", "testitem");
            defaultConfig.set("default.checklist.setting1.consolecommand", "kill %player%");
            defaultConfig.createSection("default.checklist.setting2");
            defaultConfig.set("default.checklist.setting2.containedtext", "testitem2");
            defaultConfig.set("default.checklist.setting2.consolecommand", "give %player diamond");
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
                        YamlData data = new YamlData(name,config.getString(name + ".checknbt","null"));
                        config.getConfigurationSection(name + ".checklist").getKeys(false).forEach(key -> {
                            data.checkLists.add(new CheckList(key, config.getString(name + ".checklist." + key + ".containedtext","null"),
                                    config.getString(name + ".checklist." + key + ".consolecommand","null")));
                        });
                        list.add(data);
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
                    int index = data.CheckContainedText(itemName);
                    if(index != -1){
                        p.getInventory().setItem(slot, new ItemStack(Material.AIR));
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        Bukkit.dispatchCommand(console, data.getCommand(p,index));
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
