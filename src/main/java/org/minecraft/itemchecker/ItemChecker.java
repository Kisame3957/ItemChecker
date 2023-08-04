package org.minecraft.itemchecker;

import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.minecraft.itemchecker.baseclasses.CheckListItem;
import org.minecraft.itemchecker.baseclasses.YamlData;

public final class ItemChecker extends JavaPlugin implements Listener {

    public ItemCheckManager icm = new ItemCheckManager();
    @Override
    public void onEnable() {
        // Plugin startup logic
        icm.LoadFile();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onHold(PlayerItemHeldEvent e){
        ItemStack stack = e.getPlayer().getInventory().getItem(e.getNewSlot());
        if(stack != null) {
            Player p = e.getPlayer();
            NBTItem nbtItem = new NBTItem(stack);
            p.sendMessage(nbtItem.toString());
            //p.sendMessage(String.valueOf(nbtItem.getString("ItemChecker").equals("NoReplace")));
            CheckListItem checkListItem = icm.CheckItem(stack);
            if(checkListItem != null && !nbtItem.getString("ItemChecker").equals("NoReplace")){
                p.getInventory().setItem(e.getNewSlot(), new ItemStack(Material.AIR));
                //p.sendMessage(String.valueOf(stack.getAmount()));
                String command = checkListItem.consoleCommand;
                command = command.replace("%player%",p.getName());
                command = command.replace("%amount%",String.valueOf(stack.getAmount()));
                //p.sendMessage("RunCommand: " + command);
                if(command != null) {
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    Bukkit.dispatchCommand(console, command);
                }
            }
        }
    }

    @EventHandler
    public void onSwapChestItem(InventoryClickEvent e){
        ItemStack item = null;
        if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getAmount() != 0 &&
                e.getClickedInventory().getType() == InventoryType.PLAYER && e.isShiftClick()){
            item = e.getCurrentItem();
        }
        else if(e.getCursor() != null && e.getCursor().getType() != Material.AIR && e.getCursor().getAmount() != 0 &&
                e.getClickedInventory().getType() == InventoryType.CHEST){
            item = e.getCursor();
        }
        if(item != null) {
            Chest c = (Chest) e.getView().getTopInventory().getHolder();
            if (c.getCustomName() != null) {
                if (c.getCustomName().equals(icm.DisableChestName)) {
                    NBTItem nbtItem = new NBTItem(item);
                    if (icm.CheckItem(item) != null && !nbtItem.getString("ItemChecker").equals("NoReplace")) {
                        Player p = (Player) e.getWhoClicked();
                        nbtItem.setString("ItemChecker", "NoReplace");
                        item.setItemMeta(nbtItem.getItem().getItemMeta());
                        p.sendMessage("[§6ItemChecker§f]§e自動置換対象の[§r" + item.getItemMeta().getDisplayName() + "§e]は自動置換対象から除外されました");
                    }
                } else if (c.getCustomName().equals(icm.EnableChestName)) {
                    NBTItem nbtItem = new NBTItem(item);
                    Player p = (Player) e.getWhoClicked();
                    if (icm.CheckItem(item) != null && nbtItem.getString("ItemChecker").equals("NoReplace")) {
                        nbtItem.removeKey("ItemChecker");
                        item.setItemMeta(nbtItem.getItem().getItemMeta());
                        p.sendMessage("[§6ItemChecker§f]§e[§r" + item.getItemMeta().getDisplayName() + "§e]は自動置換対象に再設定されました");
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("itemchecker")){
            Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage("§8--------------------§f[§6ItemChecker§f]§8--------------------");
                p.sendMessage("-§bプラグイン説明§f-");
                p.sendMessage("§6このプラグインは条件に一致したアイテムを自動的に削除し、コマンドを実行するプラグインです");
                p.sendMessage("-§bYamlファイルの形式§f-");
                p.sendMessage("[名前]§7:");
                p.sendMessage("  checknbt§7: §atest");
                p.sendMessage("  └§6#NBTに含まれている文字列");
                p.sendMessage("  useregularnbt§7: §atrue");
                p.sendMessage("  └§6#NBT判別に正規表現を使用するか否か(必須ではない)");
                p.sendMessage("    checklist§7:");
                p.sendMessage("      [名前]§7:");
                p.sendMessage("        containedtext§7: §atestitem");
                p.sendMessage("        ├§6#アイテム名に含まれている文字列(無い場合NBTのみで実行)");
                p.sendMessage("        └§6#containedtextが複数無い場合は一つだけ実行されます");
                p.sendMessage("        useregular§7: §atrue");
                p.sendMessage("        └§6#アイテム名判別に正規表現を使用するか否か(必須ではない)");
                p.sendMessage("        consolecommand§7: §a/kill %player%");
                p.sendMessage("        └§6#上記を両方満たした場合に実行されるコマンド");
                p.sendMessage("      [名前2]§7:");
                p.sendMessage("        containedtext§7: §atestitem2");
                p.sendMessage("        §7etc...");
                p.sendMessage("§c※出来る限り文字列は\"で囲んでください");
                p.sendMessage("-§b実行コマンドに使用可能な変数§f-");
                p.sendMessage("§a%player%§7: §6アイテム所持者");
                p.sendMessage("-§b使用可能コマンド§f-");
                p.sendMessage("§a/ic reload");
                p.sendMessage("└§6Yamlファイルのリロード");
                p.sendMessage("§a/ic show");
                p.sendMessage("└§6読み込まれているリストの表示");
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    icm.LoadFile();
                    p.sendMessage("[§6ItemChecker§f]§e リロード完了");
                    p.sendMessage("[§6ItemChecker§f]§b " + icm.list.size() + "§e個読み込まれました");
                } else if(args[0].equalsIgnoreCase("list")) {
                    int count = 0;
                    for (YamlData data:icm.list) {
                        count += data.checkListItems.size();
                    }
                    p.sendMessage("[§6ItemChecker§f]§e 現在§b" + count + "§e個読み込まれています");
                    for(YamlData data: icm.list){
                        TextComponent mainComponent = new TextComponent(data.name + ": ");
                        mainComponent.setColor(ChatColor.AQUA);
                        for(int i = 0; i<data.checkListItems.size(); i++){
                            CheckListItem checkListItem = data.checkListItems.get(i);
                            TextComponent subComponent = new TextComponent(checkListItem.name);
                            if(i < data.checkListItems.size() - 1){
                                subComponent.addExtra(", ");
                            }
                            subComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.AQUA + "NBTに" + (data.useRegularNBT?"正規表現で":"") +
                                    ChatColor.GOLD + data.checkNBT +
                                    (checkListItem.containedText!=null?(ChatColor.AQUA + "、アイテム名に" + (checkListItem.useRegularText?"正規表現で":"") +
                                    ChatColor.GOLD + checkListItem.containedText):"") +
                                    ChatColor.AQUA + "が含まれている場合、アイテムを消去し" +
                                    ChatColor.GOLD + "/" + checkListItem.consoleCommand +
                                    ChatColor.AQUA + "を実行します").create()));
                            subComponent.setColor(ChatColor.GREEN);
                            mainComponent.addExtra(subComponent);
                        }
                        p.spigot().sendMessage(mainComponent);
                    }
                    p.sendMessage("§b自動置き換え除外チェスト名: §a" + String.valueOf(icm.DisableChestName));
                    p.sendMessage("§b自動置き換え再設定チェスト名: §a" + String.valueOf(icm.EnableChestName));
                }
                else{
                    p.sendMessage("[§6ItemChecker§f]§c 不明なコマンド");
                }
            }
            return true;
        }
        return false;
    }
}
